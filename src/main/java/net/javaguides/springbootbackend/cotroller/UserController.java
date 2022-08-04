package net.javaguides.springbootbackend.cotroller;

import net.javaguides.springbootbackend.configuration.UserDetails;
import net.javaguides.springbootbackend.configuration.UserDetailsService;
import net.javaguides.springbootbackend.jwtutil.JwtUtil;
import net.javaguides.springbootbackend.model.AuthenticationResponse;
import net.javaguides.springbootbackend.model.User;
import net.javaguides.springbootbackend.repository.UserRepository;
import net.javaguides.springbootbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

//@CrossOrigin(origins ="https://reqbin.com")
//
@CrossOrigin(origins ="http://localhost:3000" , allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //public UserController(UserService userService) {
      //  this.userService = userService;
  //  }

    @GetMapping("users")
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @PostMapping("register")
    public User registerNewUse(@RequestBody User user) {
        System.out.println(user);

        if(this.userService.userExist(user)) {
            //The returned user has an id of 0
            return user;
        }


        //The user is saved, his id is created and the user is returned
        user.setUsername(user.getEmail());
        user.setRoles("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);

        return this.userService.getUserByUser(user);

    }



    @CrossOrigin(origins ="http://localhost:3000" , allowedHeaders = "*", allowCredentials = "true")
    @PostMapping("deleteusers")
    public List<User> deleteUsers(@RequestBody Object obj) {
    //public String deleteUsers(@RequestBody Object obj) {
        //An arrayList of user Ids is received
        System.out.println(obj.toString());
        System.out.println(obj);
        System.out.println(obj.getClass());

        List l = convertObjectToList(obj);

        for (int i = 0; i < l.size(); i++) {
            System.out.println("The i, " + Long.parseLong(l.get(i).toString()) );

            User userToDelete = this.userService.getUserByID(Long.parseLong(l.get(i).toString()) );

            this.userRepository.delete(userToDelete);
        }
        //return "users deleted";
        return this.userRepository.findAll();
    }


    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {

        System.out.println("The user");
        System.out.println(user.toString());

        try {
            Authentication authentication =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
        }
        catch (BadCredentialsException e){
            throw new Exception("Incorect username or password", e);
        }

        final net.javaguides.springbootbackend.configuration.UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        //begin set cookie
        ResponseCookie responseCookie = ResponseCookie.from("user-id",jwt )
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(300)
                .domain("localhost")
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
        //return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }



}
