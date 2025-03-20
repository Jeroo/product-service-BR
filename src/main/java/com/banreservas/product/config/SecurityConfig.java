// src/main/java/com/banreservas/product/config/SecurityConfig.java
package com.banreservas.product.config;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT", realmName = "banreservas-realm") // Configura la autenticación JWT
@ApplicationPath("/api")
public class SecurityConfig extends Application {
}
