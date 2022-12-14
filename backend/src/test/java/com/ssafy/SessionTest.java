//package com.ssafy;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ssafy.api.controller.SessionController;
//import com.ssafy.api.request.SessionReqDto;
//import com.ssafy.api.service.SessionService;
//import com.ssafy.db.entity.Session;
//import com.ssafy.db.entity.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc(addFilters = false)// -> webAppContextSetup(webApplicationContext)
//@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
//@WebMvcTest(SessionController.class)
//public class SessionTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private SessionService sessionService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    /* SessionService Test */
//    @Test
//    public void session_create() throws Exception{
//        // given
//        String name = "test_session";
//        LocalDate anniversary = LocalDate.parse("2023-10-05");
//        Long userId = 1L;
//        Long sessionTypeId = 1L;
//
//        // request dto
//        SessionReqDto sessionReqDto = SessionReqDto.builder()
//                .name(name)
//                .anniversary(anniversary)
//                .userId(userId)
//                .sessionTypeId(sessionTypeId).build();
//        // ???????????? ??????
//        Session sessionRes = Session.builder()
//                .name(name)
//                .anniversary(anniversary)
//                .user(User.builder()
//                        .userBirthday("12-17")
//                        .userEmail("email@naver.com")
//                        .userKakaoId(1L)
//                        .userNickname("nick-name")
//                        .userSocialRefreshToken("refresh-token")
//                        .userSocialToken("social-token")
//                        .build())
//                .sessionTypeId(sessionTypeId).build();
//        given(sessionService.createSession(any(SessionReqDto.class))).willReturn(sessionRes);
//
//        // when, then
//        this.mockMvc.perform(post("/api/session")
//                .content(objectMapper.writeValueAsString(sessionReqDto))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(document("session-create",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("anniversary").type(JsonFieldType.STRING).description("Session ?????????"),
//                                fieldWithPath("name").type(JsonFieldType.STRING).description("Session ???"),
//                                fieldWithPath("sessionTypeId").type(JsonFieldType.NUMBER).description("Session ?????? id"),
//                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("Session ??????????????? ?????? id").optional()
//                        )
////                        ,
////                        responseFields(
////                                fieldWithPath("code").type(JsonFieldType.STRING).description("????????????"),
////                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
////                                fieldWithPath("data.person.id").type(JsonFieldType.NUMBER).description("?????????"),
////                                fieldWithPath("data.person.firstName").type(JsonFieldType.STRING).description("??????"),
////                                fieldWithPath("data.person.lastName").type(JsonFieldType.STRING).description("???"),
////                                fieldWithPath("data.person.age").type(JsonFieldType.NUMBER).description("??????"),
////                                fieldWithPath("data.person.birthDate").type(JsonFieldType.STRING).description("????????????"),
////                                fieldWithPath("data.person.gender").type(JsonFieldType.STRING).description("??????"),
////                                fieldWithPath("data.person.hobby").type(JsonFieldType.STRING).description("??????")
////                        )
//                        ));
//    }
//}