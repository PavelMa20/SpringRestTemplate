package requester;
import model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.stream.Collectors;

public class Setup {

    static final String URL_USERS = "http://91.241.64.178:7081/api/users";

     public static void main(String[] args) {

         RestTemplate restTemplate = new RestTemplate();
         HttpHeaders headers = new HttpHeaders();
         Byte age = 28;
         User newUser = new User(3l, "James", "Brown", age);
         //
         //get request(getting list of all users from api) and view elements of list
         //
         User[] list = restTemplate.getForObject(URL_USERS, User[].class);
         if (list != null) {
             for (User e : list) {
                 System.out.println("User: " + e.getId() + " - " + e.getName() + " - " + e.getLastName() + " - " + e.getAge());
             }
         }
         //
         //getting value session id from "Set-Cookie"
         //
         ResponseEntity<String> forEntity = restTemplate.getForEntity(URL_USERS, String.class);
         List<String> cookies = forEntity.getHeaders().get("Set-Cookie");
         cookies.stream().forEach(System.out::println); //view
         //
         //setting session id for headers
         //
         headers.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
         headers.setContentType(MediaType.APPLICATION_JSON);
         HttpEntity<String> entity = new HttpEntity<String>(headers);

         //
         //post request(adding user to api)
         //
         HttpEntity<User> requestBody = new HttpEntity(newUser, headers);
         ResponseEntity<String> result = restTemplate.postForEntity(URL_USERS, requestBody, String.class);
         System.out.println("Status code:" + result.getStatusCode());// cheking status
         System.out.println(result.getBody());// checking body
         System.out.println(result.getHeaders());//cheking session id
         //checking full list
         ResponseEntity<String> response = restTemplate.exchange(URL_USERS, HttpMethod.GET, entity, String.class);
         System.out.println(response.getBody());

         //
         //put request(editing firstname and lastname in user with id=3)
         //
         User newUser2 = new User(3l, "Thomas", "Shelby", age);
         HttpEntity<User> requestBody2 = new HttpEntity(newUser2, headers);
         ResponseEntity<String> result2
                 = restTemplate.exchange(URL_USERS, HttpMethod.PUT, requestBody2, String.class);
         System.out.println("Status code:" + result2.getStatusCode());
         System.out.println(result2.getBody());                        //cheking status,id and body
         System.out.println(result2.getHeaders());
         //cheking full list
         ResponseEntity<String> response2 = restTemplate.exchange(URL_USERS,
                 HttpMethod.GET, entity, String.class);
         System.out.println(response2.getBody());

         //
         // delete request(removing user with id=3 from api)
         //
         ResponseEntity<String> result3
                 = restTemplate.exchange(URL_USERS+"/"+newUser2.getId(), HttpMethod.DELETE, requestBody2, String.class);
         System.out.println("Status code:" + result3.getStatusCode());
         System.out.println(result3.getBody());                             //cheking status,id and body
         System.out.println(result3.getHeaders());
         //cheking full list
         ResponseEntity<String> response3 = restTemplate.exchange(URL_USERS,
                 HttpMethod.GET, entity, String.class);
         System.out.println(response3.getBody());
    }
}
