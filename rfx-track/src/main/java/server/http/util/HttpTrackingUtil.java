package server.http.util;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.USER_AGENT;

import org.vertx.java.core.MultiMap;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.impl.Base64;

import rfx.core.util.SecurityUtil;

public class HttpTrackingUtil {
	public static final String GIF = "image/gif";
	public static final String HEADER_CONNECTION_CLOSE = "Close";
	
	public final static void setCorsHeaders(MultiMap headers){
		headers.set("Access-Control-Allow-Origin","*");
		headers.set("Access-Control-Allow-Credentials", "true");
		headers.set("Access-Control-Allow-Methods", "POST, GET");
		headers.set("Access-Control-Allow-Headers", "origin, content-type, accept, Set-Cookie");
	}
	
	public final static void trackingResponse(final HttpServerRequest req) {
		String BASE64_GIF_BLANK = "R0lGODlhAQABAIAAAAAAAAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";	
		Buffer buffer = new Buffer(Base64.decode(BASE64_GIF_BLANK));
		HttpServerResponse response = req.response();
		MultiMap headers = response.headers();
		headers.set(CONTENT_TYPE, GIF);
		headers.set(CONTENT_LENGTH, String.valueOf(buffer.length()));
		headers.set(CONNECTION, HEADER_CONNECTION_CLOSE);
		setCorsHeaders(headers);
		response.end(buffer);
	}
	
	public static String generateUUID(MultiMap headers) {
		String userAgent = headers.get(USER_AGENT);
		String logDetails = headers.get(io.netty.handler.codec.http.HttpHeaders.Names.HOST);
		String result = SecurityUtil.sha1(userAgent + logDetails + System.currentTimeMillis());
		return result;
	}
}
