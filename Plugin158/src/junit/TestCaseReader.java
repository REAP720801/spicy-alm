package junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.intland.codebeamer.wiki.plugins.Printer;
import com.intland.codebeamer.wiki.plugins.Logic;

public class TestCaseReader {

	Logic reader =null;
	
	@Before
	//called before all other Tests
	public void method()
	{
		reader = new Logic();
	}
	
	@Test
	public void testMain() {

		fail("Not yet implemented");
	}

	@Test
	public void testCheckMissingAttachment() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadAllProjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckAssociationToAttachments() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadAllAttachmentsByProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogin() {
		String serviceUrl = "http://localhost:8080/cb/remote-api";
		String login = "bond";
		String password = "007";
		//reader.login(serviceUrl, login, password);
		
		
		//reader.login(null, null, null)
		fail("Not yet implemented");
	}

	@Test
	public void testLogout() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadAllAssociations() {
		fail("Not yet implemented");
	}

}
