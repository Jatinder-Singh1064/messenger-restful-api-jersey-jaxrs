package org.learn.restws.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/injectdemo")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class InjectDemoResource {

	/*
	 * URL = http://localhost:8080/messenger/webapi/injectdemo/annotations;param=JSH
	 * 
	 * @MatrixParam = we use ; in URL to send MatrixParam param with value 'JSH'
	 * @HeaderParam = we use Request Header tab in PostMan to pass Header Key authSessionID and its Value 'anything'
	 * @CookieParam = we use Cookies tab in PostMan to add domain (localhost) and cookie as 'name' and its value 'anything'
	 * 
	 */
	
	@GET
	@Path("annotations")
	public String getParamsUsingAnnotations(@MatrixParam("param") String matrixParam,
											@HeaderParam("authSessionID") String header,
											@CookieParam("name") String cookie) {
		
		return "Matrix Param: " + matrixParam + " Header Param: " + header + " Cookie Param: " + cookie;
	}
	
	@GET
	@Path("context")
	public String getParamsUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) {
		String path = uriInfo.getAbsolutePath().toString();
		String cookies = httpHeaders.getCookies().toString();
		return "Path: " + path + " Cookies: " + cookies;
	}
}
