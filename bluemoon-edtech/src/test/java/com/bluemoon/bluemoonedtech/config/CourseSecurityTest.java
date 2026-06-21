package com.bluemoon.bluemoonedtech.config;

import com.bluemoon.bluemoonedtech.controller.PublicCourseController;
import com.bluemoon.bluemoonedtech.controller.PublicLessonController;
import com.bluemoon.bluemoonedtech.dto.CourseResponseDTO;
import com.bluemoon.bluemoonedtech.security.CustomUserDetailsService;
import com.bluemoon.bluemoonedtech.security.JwtAuthenticationEntryPoint;
import com.bluemoon.bluemoonedtech.security.JwtAuthenticationFilter;
import com.bluemoon.bluemoonedtech.security.JwtUtils;
import com.bluemoon.bluemoonedtech.service.PublicCourseService;
import com.bluemoon.bluemoonedtech.service.PublicLessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        PublicCourseController.class,
        PublicLessonController.class
})
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class
})
class CourseSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublicCourseService publicCourseService;

    @MockitoBean
    private PublicLessonService publicLessonService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void courseMetadataIsPublic() throws Exception {
        when(publicCourseService.getAllPublishedCourses())
                .thenReturn(List.of(CourseResponseDTO.builder()
                        .id(1L)
                        .title("Public course")
                        .published(true)
                        .build()));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk());
    }

    @Test
    void lessonContentRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/courses/1/lessons"))
                .andExpect(status().isUnauthorized());
    }
}
