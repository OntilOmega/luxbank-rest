package lu.luxbank.restwebservice.security;

import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.security.model.LuxBankUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtils jwtUtils;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private InvalidTokenService invalidTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getTokenFromHeader(request);
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                if (invalidTokenService.isTokenInvalid(jwt)) {
                    throw new LuxBankGeneralException(HttpStatus.UNAUTHORIZED,
                            "Token was invalidated");
                }
                String username = jwtUtils.getUsernameFromToken(jwt);
                LuxBankUser luxBankUser = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        luxBankUser, null, luxBankUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (LuxBankGeneralException ex) {
            log.info(ex.getErrorMsg());
        } catch (Exception ex) {
            log.info("We have en error during authentication process", ex);
        }
        filterChain.doFilter(request, response);
    }
}
