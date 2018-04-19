package com.example.webApp;

import com.example.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.keycloak.KeycloakPrincipal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.security.Principal;


@Controller
public class WebAppController {
//    @Autowired
//    private CustomerDAO customerDAO;
    String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown hostname");
    @Autowired
    private UserService userService;


    @GetMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("hostname", hostname);
        return "external";
    }

    @GetMapping(path = "/login")
    public String login(Principal principal, Model model) {
        if (principal!=null){
            model.addAttribute("username", principal.getName());
            return "login";
        }
        else {
            model.addAttribute("username", "damn");
            return "login";
        }

    }

    @GetMapping(path = "/users")
    public String users(Principal principal, Model model) throws IOException, UserService.Failure {
        //addCustomers();
       // Iterable<Customer> customers = customerDAO.findAll();
        System.out.println("Get users!");
        Iterable<User> users = userService.getAllUsersFromDB(principal);
        model.addAttribute("users", users);
        model.addAttribute("username", principal.getName());
        return "users";
    }

    // add customers for demonstration
    public void addCustomers() {

        /*Customer customer1 = new Customer();
        customer1.setAddress("1111 foo blvd");
        customer1.setName("Foo Industries");
        customer1.setServiceRendered("Important services");
        customerDAO.save(customer1);

        Customer customer2 = new Customer();
        customer2.setAddress("2222 bar street");
        customer2.setName("Bar LLP");
        customer2.setServiceRendered("Important services");
        customerDAO.save(customer2);

        Customer customer3 = new Customer();
        customer3.setAddress("33 main street");
        customer3.setName("Big LLC");
        customer3.setServiceRendered("Important services");
        customerDAO.save(customer3);*/
    }

    //logout
    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "external";
    }

    //http://auth-server/auth/realms/{realm-name}/tokens/logout?redirect_uri=encodedRedirectUri
}
