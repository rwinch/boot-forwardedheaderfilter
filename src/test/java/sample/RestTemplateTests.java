package sample;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTemplateTests {
	@Autowired
	TestRestTemplate rest;
	@LocalServerPort
	int port;
	String redirectUrl = "/redirect";

	@Test
	public void locationLogin() throws Exception {
		ResponseEntity<String> result = locationTo("/login");
		assertThat(result).hasRedirectOf("/login");
	}

	@Test
	public void redirectLogin() throws Exception {
		ResponseEntity<String> result = redirectTo("/login");
		assertThat(result).hasLocalRedirectOf("/login");
	}

	@Test
	public void redirectSlashSlashLogin() throws Exception {
		ResponseEntity<String> result = redirectTo("//example.com/logout");
		assertThat(result).hasRedirectOf("http://example.com/logout");
	}

	@Test
	public void redirectSlashSlashParentDotDotLogin() throws Exception {
		ResponseEntity<String> result = redirectTo("//example.com/parent/../logout");
		assertThat(result).hasRedirectOf("http://example.com/parent/../logout");
	}

	@Test
	public void redirectFoo() throws Exception {
		ResponseEntity<String> result = redirectTo("/foo");
		assertThat(result).hasLocalRedirectOf("/foo");
	}

	@Test
	public void redirectDotDotFoo() throws Exception {
		ResponseEntity<String> result = redirectTo("../foo");
		assertThat(result).is5xx();
	}

	@Test
	public void redirectParentDotDotFoo() throws Exception {
		redirectUrl += "/";
		ResponseEntity<String> result = redirectTo("../foo");
		assertThat(result).hasLocalRedirectOf("/foo");
	}

	@Test
	public void redirectParentAndFileDotDotFoo() throws Exception {
		redirectUrl += "/parent";
		ResponseEntity<String> result = redirectTo("../foo");
		assertThat(result).hasLocalRedirectOf("/foo");
	}

	private ResponseEntity<String> locationTo(String to) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("l", to);
		ResponseEntity<String> result = rest.postForEntity("/location", map, String.class);
		return result;
	}


	private ResponseEntity<String> redirectTo(String to) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("l", to);
		ResponseEntity<String> result = rest.postForEntity(redirectUrl, map, String.class);
		return result;
	}

	private ResponseEntityAssertion assertThat(ResponseEntity<?> result) {
		return new ResponseEntityAssertion(result);
	}

	class ResponseEntityAssertion extends AbstractAssert<ResponseEntityAssertion, ResponseEntity<?>> {

		public ResponseEntityAssertion(ResponseEntity<?> actual) {
			super(actual, ResponseEntityAssertion.class);
		}

		public ResponseEntityAssertion is5xx() {
			Assertions.assertThat(this.actual.getStatusCode().is5xxServerError()).isTrue();
			return this;
		}

		public ResponseEntityAssertion hasLocalRedirectOf(String path) {
			String url = "http://localhost:"+ port + path;
			Assertions.assertThat(this.actual.getHeaders().getLocation()).hasToString(url);
			return this;
		}

		public ResponseEntityAssertion hasRedirectOf(String url) {
			Assertions.assertThat(this.actual.getHeaders().getLocation()).hasToString(url);
			return this;
		}
	}
}
