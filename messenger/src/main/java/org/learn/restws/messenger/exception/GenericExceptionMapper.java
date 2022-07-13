package org.learn.restws.messenger.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.learn.restws.messenger.model.ErrorMessage;

//@Provider // commented as this is kind of catch-all generic exception
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable ex) {
		
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 500, "http://restws.learn.org");
		
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(errorMessage)
				.build();
	}

}