package vorfuerhung;

import remoteApi.RAReader;

public class TestClass {

	public void test ()
	{
		RAReader raReader = new RAReader ();
		raReader.readAllAssociations();
	}
}
