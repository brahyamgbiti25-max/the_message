package by.Brahyam.Garcia.Blas.the.message.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtUtil {
    private  static final  long JWT_TOKEN_VALIDITY=30L*24*60*60*1000;
    @Value("${jwt.secret:TheTalentIsn'tRequestTogrowLikeDeveloper}")
    private  String secret;
    private SecretKey getSingingKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    public  String getUsernameFromToken(String token){
        return getClaimFrontToken(token,Claims::getSubject);
    }
    public String getRoleFromToken(String token){
        return getClaimFrontToken(token, claims->claims.get("role",String.class));
    }
    public Date getExpirationDateFromToken(String token){
        return getClaimFrontToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFrontToken(String token, Function<Claims,T> claimResolver) {
        final  Claims claims=getAllClaimFrontToken(token);
        return  claimResolver.apply(claims);
    }

    private Claims getAllClaimFrontToken(String token) {
        return Jwts.parser().verifyWith(getSingingKey()).build().parseSignedClaims(token).getPayload();
    }

    private  Boolean isTokenExpired(String token){
        final Date expiration= getExpirationDateFromToken(token);
        return  expiration.before(new Date());
    }
    public  String generateToken(String username,String role){
        Map<String , Object> claims= new HashMap<>();
        claims.put("role",role);
        return doGenerateToken(claims,username);
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder().claims(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
                .signWith(getSingingKey())
                .compact();
    }
    public Boolean validateToken(String token){
        try{
            getAllClaimFrontToken(token);
            return  !isTokenExpired(token);
        }catch (Exception e){
            System.out.println("JWT Validation Error: "+e.getMessage());
            return false;
        }
    }
}
