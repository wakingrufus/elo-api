import com.almworks.sqlite4java.SQLite;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.wakingrufus.elo.Main;
import com.github.wakingrufus.elo.auth.LoginRequest;
import com.github.wakingrufus.elo.auth.Session;
import com.github.wakingrufus.elo.league.GameType;
import com.github.wakingrufus.elo.league.League;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import com.github.wakingrufus.elo.user.User;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class IntegrationTest {

    @Before
    public void setup() {
        Map<String, String> env = new HashMap<>();
        env.put("AWS_ACCESS_KEY_ID", "keyid");
        env.put("AWS_SECRET_ACCESS_KEY", "key");
        env.put("DB_URL", "http://127.0.0.1:3210");
        setEnv(env);
    }

    private void setEnv(Map<String, String> newenv) {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            try {
                Class[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> env = System.getenv();
                for (Class cl : classes) {
                    if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newenv);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void integrationTest() throws Exception {
        SQLite.setLibraryPath((this.getClass().getClassLoader().getResource("lib").getPath()));
        final String[] localArgs = {"-inMemory", "-port", "3210"};
        DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();


        final String baseUrl = "http://localhost:9005";
        Main.main(new String[0]);


        User user = User.builder().name("rufus").password("pass").email("wakingrufus@gmail.com").name("John Burns").build();
        ObjectMapper mapper = new ObjectMapperFactory().buildObjectMapper();

        Client client = JerseyClientBuilder.newBuilder().build();
        JacksonJsonProvider jacksonJaxbJsonProvider = new JacksonJsonProvider();
        jacksonJaxbJsonProvider.setMapper(mapper);
        client.register(jacksonJaxbJsonProvider);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // create user
        log.info("Create a user");
        WebTarget target = client.target(baseUrl).path("elo-api/user");
        User createdUser = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE), User.class);
        Assert.assertEquals("create user returns 201", user.getEmail(), createdUser.getEmail());

        // get by id (unauth)
        log.info("fetch a user while not logged in");
        target = client.target(baseUrl).path("elo-api/user/" + createdUser.getId());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());


        // create session
        log.info("log in");
        LoginRequest loginRequest = new LoginRequest("pass", "wakingrufus@gmail.com");
        target = client.target(baseUrl).path("elo-api/session");
        Session createdSession = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(loginRequest, MediaType.APPLICATION_JSON_TYPE), Session.class);
        log.info("Session id:" + createdSession.getId());

        // get by id (auth)
        log.info("get by user id (authorized)");
        target = client.target(baseUrl).path("elo-api/user/" + createdUser.getId());
        User getUser = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + createdSession.getId())
                .get(User.class);
        Assert.assertNotNull("user is found", getUser);
        Assert.assertEquals(user.getEmail(), getUser.getEmail());

        /*
         * League Tests
         */

        log.info("get game types");
        target = client.target(baseUrl).path("elo-api/leagues/types");
        GameType[] gameTypesArr = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + createdSession.getId()).get(GameType[].class);

        GameType gameType = (gameTypesArr[0]);
        log.info("create league");
        League newLeague = League.builder()
                .gameType(gameType)
                .kfactorBase(32)
                .name("test singles league")
                .startingRating(1500)
                .xi(1000)
                .trialPeriod(10)
                .trialKFactorMultiplier(2)
                .teamSize(1)
                .build();
        target = client.target(baseUrl).path("elo-api/leagues");
        League createdLeague = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + createdSession.getId())
                .post(Entity.entity(newLeague, MediaType.APPLICATION_JSON_TYPE), League.class);
        log.info("league id:" + createdLeague.getId());

        log.info("get league by id");
        target = client.target(baseUrl).path("elo-api/leagues/" + createdLeague.getId());
        League leagueFromGet = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + createdSession.getId()).get(League.class);
        Assert.assertEquals(createdLeague.getId(), leagueFromGet.getId());

        log.info("get leagues by game type id");
        target = client.target(baseUrl).path("elo-api/leagues").queryParam("typeId", createdLeague.getGameType().getId());
        League[] leaguesByGameTypeArr = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + createdSession.getId()).get(League[].class);
        List<League> leaguesList = Arrays.asList(leaguesByGameTypeArr);
        Assert.assertEquals(1, leaguesList.size());


        /*
         * Log out tests
         */
        log.info("log out");
        target = client.target(baseUrl).path("elo-api/session/" + createdSession.getId());
        Response logoutResponse = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + createdSession.getId()).delete();
        Assert.assertEquals("logout returns 204", 204, logoutResponse.getStatus());


        log.info("fetch a user after logout");
        target = client.target(baseUrl).path("elo-api/user/" + createdUser.getId());
        Response getByIdResponseAfterLoggingOut = target.request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
        Assert.assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), getByIdResponseAfterLoggingOut.getStatus());

        server.stop();
    }
}
