import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.DaaS.core.objects.Instance;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.*;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import static com.jayway.restassured.module.mockmvc.response.MockMvcResponse.*;

import static com.jayway.restassured.module.mockmvc.response.MockMvcResponse.*;
import static org.hamcrest.Matchers.*;

public class InstanceRestControllerTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Autowired
	WebApplicationContext wac;
	
	
	
	private List<Long> instanceIds = new ArrayList<Long>();
	
	@Before
	public void setUp() throws Exception{
		RestAssuredMockMvc.reset();
	}
	
	
	
	@Test
	public void testSaveInstance() {

		Instance instance = new Instance();
		instance.setImageId("ami-5189a661");
		instance.setInstanceType("t2.micro");
		instance.setKeyName("Eragon");
		instance.setMaxCount(1);
		instance.setMinCount(1);
		instance.setSecurityGroup("Java Security group");
		instance.setWorkspaceName("Java workspace");
		instance.setWorkspaceType("AWS");
		
		
		
		final MockMvcResponse response = 
				given().
				log().all().
				contentType("application/json").
				body(instance).
				when().
				post("/createInstance").thenReturn();
		
		assertThat(response.getStatusCode(),equalTo(201));
		
		instanceIds.add(instance.getInstanceId());
		
	}

	
	
	@Test
	public void testGetInstance() {
		
		if(instanceIds.isEmpty())
		{
			testSaveInstance();
		}
		
		int instanceId = Math.toIntExact(instanceIds.get(0));
		
		get("/getInstance/{instance_id", 1L).
		then().assertThat().
		statusCode(200).
		contentType(ContentType.JSON).
		body("instanceId",equalTo(instanceId));
		
		
	}

	
	
	@Test
	public void testGetAllInstances() {
		
		if(instanceIds.size()<2)
		{
		testSaveInstance();
		testSaveInstance();
		}
		
		
		get("/getAllInstances/").
		then().assertThat().
		statusCode(200).
		contentType(ContentType.JSON).
		body("instanceId",hasItems(Math.toIntExact(instanceIds.get(0)),Math.toIntExact(instanceIds.get(1)) ));
		
		
	}

	
	@Test
	public void testDeleteInstance() {
		
		if(instanceIds.isEmpty()){
			testSaveInstance();
		}
		
		
		int instanceId = Math.toIntExact(instanceIds.get(0));
		
		delete("/deleteInstance/{instance_id}",instanceId).
		then().assertThat().
		statusCode(204);
		
		get("/getInstance/{instance_id}",instanceId).
		then().assertThat().
		statusCode(200).
		body(isEmptyString());
		
		
	}
		
	
	
	@Test
	public void testGetInstanceCount() {
		
		final MockMvcResponse response = get("/getAllInstances");
		List<Instance> instanceList = Arrays.asList(response.getBody().as(Instance[].class));
		
		get("getInstanceCount").then().assertThat().statusCode(200).
		body(equalTo(Integer.toString(instanceList.size())));
	}
		
	
}
