package net.bytecms.security.custom;

/**
 * @Author Luo.z.x
 * @Date 2021/3/4 16:15
 * @Version 1.0
 */
import net.bytecms.core.handler.CustomException;
import net.bytecms.core.utils.BaseContextKit;
import net.bytecms.core.utils.Checker;
import net.bytecms.core.utils.SpringContextHolder;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestMatcher matcher;
    private JwtTokenStore tokenStore;
    private AbsCustomJwtHandler customJwtHandler;

    public void setMatcher(RequestMatcher matcher) {
        this.matcher = matcher;
    }

    public void setTokenStore(JwtTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void setCustomJwtHandler(AbsCustomJwtHandler customJwtHandler) {
        this.customJwtHandler = customJwtHandler;
    }

    public void setTokenExtractor(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }

    public RequestMatcher getMatcher() {
        return this.matcher;
    }

    public JwtTokenStore getTokenStore() {
        return this.tokenStore;
    }

    public AbsCustomJwtHandler getCustomJwtHandler() {
        return this.customJwtHandler;
    }

    TokenExtractor tokenExtractor = (TokenExtractor)SpringContextHolder.getBean(TokenExtractor.class);

    public TokenExtractor getTokenExtractor() {
        return this.tokenExtractor;
    }

    public CustomJwtAuthenticationFilter(RequestMatcher matcher, AbsCustomJwtHandler customJwtHandler) {
        this.matcher = matcher;
        this.customJwtHandler = customJwtHandler;
    }

    public CustomJwtAuthenticationFilter(RequestMatcher matcher, JwtTokenStore tokenStore, AbsCustomJwtHandler customJwtHandler) {
        this.matcher = matcher;
        this.tokenStore = tokenStore;
        this.customJwtHandler = customJwtHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = this.tokenExtractor.extract(request);
        if (Checker.BeNull(authentication))
            throw new AccessDeniedException("Invalid Jwt is invalid!");
        try {
            this.customJwtHandler.handlerJwtToken(authentication, this.tokenStore);
        } catch (CustomException error) {
            throw new AccessDeniedException(error.getMessage());
        }
        try {
            filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
        } finally {
            BaseContextKit.remove();
        }
    }
}