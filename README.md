# ForoHub

Challenge design by AluraLatam to put in practice the acknowledge acquired during the Srping Boot Rest curses

## Badges


![Project Finish](https://img.shields.io/badge/DataBase-green.svg)
![GPLv3 License](https://img.shields.io/badge/CRUD-SPRINGBOOT%20REST-red.svg)
![AGPL License](https://img.shields.io/badge/JAVA-blue.svg)


## Features

- SpringBoot
- Flyway
- SpringSecurity
- MySQL
- Lombok

## Project Description

Design the BackEnd of a web application with SpringBoot, were it is made the record,delate,list and update of topics of a comminity od users that it´s information is save in a DataBase.

One of the Business Rules was to design the security of access for the users in the app. So I use the tools that offers Spring Security to design the Authentication and Authorization for the users in the app.

## Index

- New Topic Record
- List All Topics
- Detail a Topic
- Update a Topic
- Delete a Topic
- Authentication with Spring Security
- Generate a Token JWT
- Authentication with JWT


## Project Design
### New Topic Record

```bash
@Transactional
@PostMapping
public ResponseEntity nuevoTopico (@RequestBody @Valid DatosNuevoTopico datos, UriComponentsBuilder uriComponentsBuilder){

        var topico = new Topico(datos);
                repository.save(topico);

        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));
}  
```

#### List all Topic

```bash
@GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar (Pageable paginacion) {
       var page = repository.findAllByActivoTrue(paginacion).map(DatosListaTopico :: new);
       return ResponseEntity.ok(page);
    }
```

### Detail Topic

```bash
@GetMapping ("/{id}")
    public ResponseEntity<DatosDetalleTopico> detallarTopico (@PathVariable Long id){
        return repository.findById(id)
                .map(topico -> ResponseEntity.ok(topico.detalleTopicoSeleccionado()))
                .orElse(ResponseEntity.notFound().build());
    }
```

### Update Topic
```bash
@Transactional
    @PutMapping ("/{id}")
    public ResponseEntity actualizar (@PathVariable Long id,@RequestBody @Valid DatosActualizarTopico datos){
        var topico = repository.getReferenceById(id);

        topico.actualizarinformacion(datos);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }
```

### Delete Topic
```bash
@Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id){
        var topico = repository.getReferenceById(id);

        topico.eliminar();

        return ResponseEntity.noContent().build();

    }
````

### Authentication with Spring Security

#### Authentication Controller
```bash
@RestController
@RequestMapping ("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAuntenticacion datos){
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(),datos.contrasena());
        var autenticacion = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generarToken((Usuario) autenticacion.getPrincipal());

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}
```

#### SecurityConfiguration
```bash
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{

        return http.csrf(csrf-> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST,"/login").permitAll();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
}
```

### Generate a Token JWT
For the Token JWT we need in our pom.xml
```bash
<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>4.5.0</version>
		</dependency>
```

A TokenService
```bash
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generarToken (Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API forohub")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(fechaExpiracion())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("error al generar el Token JWT",exception);
        }
    }

    private Instant fechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
```

### Authentication with JWT

```bash
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private IUsuarioRepositury repositury;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);
        if(tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            var usuario = repositury.findByLogin(subject);

            var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Usuario Logeado");
        }


        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader= request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ","");
        }
        return null;

    }
```
