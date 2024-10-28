package task_manager_back.task_manager_back.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth/")) {
            filterChain.doFilter(request, response); // Permite continuar sin validación
            return;
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: No Authorization header");
            return; // Detiene la ejecución si no hay cabecera de autorización
        }
        // Extrae el token JWT de la cabecera Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Elimina el prefijo "Bearer "
            username = jwtUtil.extractUsername(jwt);  // Extrae el email del token
        }

        // Valida el token y establece la autenticación en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continúa con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
