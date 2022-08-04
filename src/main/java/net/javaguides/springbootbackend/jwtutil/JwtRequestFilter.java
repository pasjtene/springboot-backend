package net.javaguides.springbootbackend.jwtutil;

import net.javaguides.springbootbackend.configuration.UserDetails;
import net.javaguides.springbootbackend.configuration.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

@Service
@CrossOrigin(origins ="http://localhost:3000" , allowedHeaders = "*", allowCredentials = "true")
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie->name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        System.out.println("The cookie....");

        //System.out.println( Arrays.stream(request.getCookies()));

        System.out.println("The cookie....2...");

        //System.out.println(this.getCookieValue(request, "user-id"));

        Cookie cookie[]=request.getCookies();
        Cookie cook;
        String uname= null,pass="";

        if (cookie == null) {
            System.out.println("No cookies...");
            System.out.println("No cookies...");
        } else  {
            System.out.println("No cookies...the type..");
            System.out.println(cookie.getClass().getName());

            for (int i = 0; i < cookie.length; i++) {
                cook = cookie[i];
                if(cook.getName().equalsIgnoreCase("user-id")) {
                    uname=cook.getValue();
                    System.out.println(cook.getValue());
                } else  {
                    System.out.println(cook.getValue());
                }

            }

        }

        System.out.println("The user jwt cookie");
        System.out.println(uname);

        System.out.println("The Authorization header");





        String username = null;
        String jwt = null;


/**
        //if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            if(authorizationHeader != null ) {
                System.out.println("The auth header is here");
                System.out.println(authorizationHeader);
            //jwt = authorizationHeader.substring(7);
                jwt = authorizationHeader;
            username = jwtUtil.extractUsername(jwt);
        }
            */

        if(uname != null) {
            //We have a cookie
            jwt = uname;
            username = jwtUtil.extractUsername(jwt);
        }


        //username = uname;

        if((username !=null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
            //jwt = uname;
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken
                        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);

    }
}
