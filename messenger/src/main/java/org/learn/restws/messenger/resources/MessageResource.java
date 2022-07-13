package org.learn.restws.messenger.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.learn.restws.messenger.model.Message;
import org.learn.restws.messenger.resources.beans.MessageFilterBean;
import org.learn.restws.messenger.service.MessageService;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.Response.Status;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)	//@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class MessageResource {
	
	MessageService messageService = new MessageService();
	
	/*	 
	 @GET
	public List<Message> getMessages(@QueryParam("year") int year,
										@QueryParam("start") int start,
										@QueryParam("size") int size) 
	{	
		if(year > 0) {
			return messageService.getAllMessagesForYear(year);
		}
		
		if(start >= 0 && size > 0 ) {
			return messageService.getAllMessagesPaginated(start, size);
		}
		
		return messageService.getAllMessages();
	}	 
	 */
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getJsonMessages(@BeanParam MessageFilterBean filterBean) 
	{	
		System.out.println("JSON method called");
		if((filterBean.getYear()) > 0) {
			return messageService.getAllMessagesForYear(filterBean.getYear());
		}
		
		if((filterBean.getStart() >= 0) && (filterBean.getSize() > 0) ) {
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return messageService.getAllMessages();
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Message> getXmlMessages(@BeanParam MessageFilterBean filterBean) 
	{		
		System.out.println("XML method called");
		if((filterBean.getYear()) > 0) {
			return messageService.getAllMessagesForYear(filterBean.getYear());
		}
		
		if((filterBean.getStart() >= 0) && (filterBean.getSize() > 0) ) {
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return messageService.getAllMessages();
	}
	
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(id);
		message.addLink(getUriForSelf(uriInfo, message), "self");	
		message.addLink(getUriForProfile(uriInfo, message), "profile");	
		message.addLink(getUriForComments(uriInfo, message), "comments");	
		return message;
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
				.path(MessageResource.class, "getCommentResource")
				.path(CommentResource.class)
				.resolveTemplate("messageId", message.getId())
				.build()
				.toString();
		return uri;
	}

	private String getUriForProfile(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(ProfileResource.class)
				.path(message.getAuthor())
				.build()
				.toString();
		return uri;
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
						.path(MessageResource.class)
						.path(Long.toString(message.getId()))
						.build()
						.toString();
		return uri;
	}
	
	/*
	@POST
	public Message addMessage(Message message) {
		return messageService.addMessage(message);
	}

	//To return the custom Status Code 201 along with entity in the Response Header
	@POST
	public Response addMessage(Message message) {
		Message newMessage = messageService.addMessage(message);
		return Response.status(Status.CREATED)
						.entity(newMessage)
						.build();
	}
		
	//To return the custom Status Code 201, Location URI along with entity in the Response Header
	@POST
	public Response addMessage(Message message) throws URISyntaxException {
		Message newMessage = messageService.addMessage(message);
		return Response.created(new URI("/messenger/webapi/messages/" + newMessage.getId()))
						.entity(newMessage)
						.build();
	}
	*/
	
	//BETTER APPROACH - To return the custom Status Code 201, Location URI along with entity in the Response Header
	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo) throws URISyntaxException {
		System.out.println(uriInfo.getAbsolutePath());
		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
						.entity(newMessage)
						.build();
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return messageService.updateMessage(message);
	}	
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long id) {
		messageService.removeMessage(id);
	}

	//Passing control to CommentsResource - SubResource
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
	
}
