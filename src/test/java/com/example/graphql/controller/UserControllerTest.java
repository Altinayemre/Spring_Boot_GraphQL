package com.example.graphql.controller;

import com.example.graphql.dto.request.UserCreateRequest;
import com.example.graphql.dto.response.UserDto;
import com.example.graphql.model.Role;
import com.example.graphql.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureGraphQlTester
class UserControllerTest {
    @Autowired
    GraphQlTester graphQlTester;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        createUser(new UserCreateRequest("Emre", "emre@outlook.com", Role.ADMIN));
        createUser(new UserCreateRequest("Hakan", "hakan@outlook.com", Role.USER));
        createUser(new UserCreateRequest("Mehmet", "mehmet@outlook.com", Role.USER));
    }

    @Test
    @Order(1)
    void when_getAllUsers_shouldReturnUserDtoList() {
        // language=graphql
        String document = """
                query {
                  getAllUsers {
                    id
                  	userName
                    mail
                    role
                    created
                    updated
                  }
                }
                """;

        graphQlTester.document(document)
                .execute()
                .path("getAllUsers")
                .entityList(UserDto.class)
                .hasSize(3);
    }

    @Test
    @Order(2)
    void when_getUserByIdExistsIsValid_ShouldReturnUserDto() {
        // language=graphql
        String document = """
                query getUserByIdUserDto($id:ID!) {
                    getUserById(id:$id) {
                        id
                        userName
                        mail
                        role
                        created
                        updated
                    }
                }
                """;

        graphQlTester.document(document)
                .variable("id", 1)
                .execute()
                .path("getUserById")
                .entity(UserDto.class)
                .satisfies(user -> {
                    assertEquals("Emre", user.userName());
                    assertEquals("emre@outlook.com", user.mail());
                    assertEquals(Role.ADMIN, user.role());
                });
    }

    @Test
    @Order(3)
    void when_createUser_shouldCreateNewUserAndReturnUserDto() {
        int currentUserCount = userService.getAllUsers().size();
        // language=graphql
        String document =
                """
                        mutation {
                          createUser(userCreateRequest: {userName: "Cem", mail: "cem@outlook.com", role: ADMIN}) {
                            id   
                            userName
                            mail
                            role     
                            created
                          }
                        }
                        """;

        graphQlTester
                .document(document)
                .execute()
                .path("createUser")
                .entity(UserCreateRequest.class)
                .satisfies(
                        x -> {
                            assertEquals("Cem", x.userName());
                            assertEquals("cem@outlook.com", x.mail());
                            assertEquals(Role.ADMIN, x.role());
                        });

        assertEquals(currentUserCount + 1, userService.getAllUsers().size());
    }

    @Test
    @Order(4)
    void when_updateUser_shouldUpdateExistingUserAndReturnUserDto() {
        // language=graphql
        String document =
                """
                        mutation {
                          updateUser(userUpdateRequest: {id:1,userName: "Emre0", mail: "emre0@gmail.com", role: ADMIN}) {
                            id
                            userName
                            mail
                            role
                            updated
                          }
                        }
                        """;

        graphQlTester
                .document(document)
                .execute()
                .path("updateUser")
                .entity(UserDto.class);

        UserDto updatedUser = userService.getUserById(1L).get();
        assertEquals("Emre0", updatedUser.userName());
        assertEquals("emre0@gmail.com", updatedUser.mail());
    }

    @Test
    @Order(5)
    void when_deleteUserIsValid_shouldExistingUserRemove() {
        int currentUser = userService.getAllUsers().size();
        // language=graphql
        String document =
                """
                        mutation deleteUser($id:ID!){
                          deleteUser(id:$id) 
                        }
                        """;
        graphQlTester
                .document(document)
                .variable("id", 3)
                .executeAndVerify();

        assertEquals(currentUser - 1, userService.getAllUsers().size());
    }

    private void createUser(UserCreateRequest userCreateRequest) {

        String document = """
                mutation{
                  createUser(userCreateRequest:{userName:"%s",mail:"%s",role:%s}) { 
                    id
                    userName
                    role
                    created
                    updated
                  }
                }
                """.formatted(userCreateRequest.userName(), userCreateRequest.mail(), userCreateRequest.role());

        graphQlTester.document(document).execute().path("createUser");
    }
}