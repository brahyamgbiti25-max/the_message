package by.Brahyam.Garcia.Blas.the.message.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter.
 * This component intercepts every incoming HTTP request (extending OncePerRequestFilter)
 * to check for a valid JSON Web Token (JWT). If a valid token is found, it authenticates
 * the user inside the Spring Security context.
 */
@Component
public class JwtAutenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Core filter method executed on every request. It extracts the JWT token,
     * retrieves the username, validates the token, and sets up security authentication.
     *
     * @param request     The incoming HTTP request
     * @param response    The outgoing HTTP response
     * @param filterChain The filter chain to pass the request along
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain
            filterChain) throws ServletException, IOException {

        // Step 1: Extract the JWT string from the request (Header or Query Parameter)
        String jwt = extractJwtToken(request);
        String username = null;

        // Step 2: If token exists, extract the username (subject) from it
        if (jwt != null) {
            username = jwtUtil.getUsernameFromToken(jwt);
        }

        // Step 3: If username is found and user is not already authenticated in the context
        if (shouldProcessAuthentication(username)) {
            processAuthentication(request, jwt, username);
        }

        // Step 4: Continue the filter chain so the request reaches the Controller
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the incoming request.
     * It checks two places:
     * 1. The traditional "Authorization" header (Bearer token) used by REST APIs.
     * 2. A query parameter named "token" (e.g., ?token=xyz) used during WebSocket handshakes.
     *
     * @param request The HTTP request
     * @return The JWT token string, or null if not found
     */
    private String extractJwtToken(HttpServletRequest request) {
        // Check Authorization Header
        final String authorizationReader = request.getHeader("Authorization");
        if (authorizationReader != null && authorizationReader.startsWith("Bearer ")) {
            return authorizationReader.substring(7); // Remove "Bearer " prefix
        }

        // Fallback: Check Query Parameter (crucial for WebSockets)
        /**else if (request.getRequestURI().startsWith("/ws")) {
            String tokenParam = request.getParameter("token");
            if (tokenParam != null && !tokenParam.isEmpty()) {
                return tokenParam;
            }
        }*/


        return null;
    }

    /**
     * Checks if authentication should be processed.
     * Ensures we have a username and that the SecurityContext doesn't already have an active authentication.
     *
     * @param username The extracted username
     * @return true if authentication can proceed, false otherwise
     */
    private boolean shouldProcessAuthentication(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    /**
     * Validates the token and sets up the user session in the security context if valid.
     *
     * @param request  The HTTP request
     * @param jwt      The JWT token
     * @param username The username extracted from the token
     */
    private void processAuthentication(HttpServletRequest request, String jwt, String username) {
        // Validate signature and expiration date
        if (jwtUtil.validateToken(jwt)) {
            UserDetails userDetails = createUserDetailsFromToken(jwt, username);
            setAuthenticationInContext(request, userDetails);
        }
    }

    /**
     * Builds a Spring Security UserDetails object using information embedded inside the JWT token.
     *
     * @param jwt      The JWT token
     * @param username The username
     * @return UserDetails containing username, empty password, and roles/authorities
     */
    private UserDetails createUserDetailsFromToken(String jwt, String username) {
        // Extract role claim from token and map it to a Spring Security Authority (e.g., ROLE_USER)
        String role = jwtUtil.getRoleFromToken(jwt);
        return User.builder()
                .username(username)
                .password("") // Credentials are empty because token already authenticates the user
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)))
                .build(); // FIXED: Added .build() here which was missing in your original snippet
    }

    /**
     * Officially authenticates the user within Spring Security by placing the token
     * into the global SecurityContextHolder.
     *
     * @param request     The HTTP request
     * @param userDetails The built user details containing roles
     */
    private void setAuthenticationInContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // Credentials are set to null since authentication is token-based
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Store authentication token globally for this request lifecycle
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}