package seg3x02.tempconverterapi

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TemperatureConverterSecurityTests(@Autowired private val mockMvc: MockMvc) {

    @Test
    fun `unauthenticated conversion request is rejected with basic challenge`() {
        mockMvc.perform(get("/temperature-converter/celsius-fahrenheit/50"))
            .andExpect(status().isUnauthorized)
            .andExpect(header().string("WWW-Authenticate", containsString("Basic")))
    }

    @Test
    fun `user1 can convert celsius to fahrenheit`() {
        mockMvc.perform(
            get("/temperature-converter/celsius-fahrenheit/50")
                .with(httpBasic("user1", "pass1"))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().string("122.0"))
    }

    @Test
    fun `user2 can convert fahrenheit to celsius`() {
        mockMvc.perform(
            get("/temperature-converter/fahrenheit-celsius/212")
                .with(httpBasic("user2", "pass2"))
        )
            .andExpect(status().isOk)
            .andExpect(content().string("100.0"))
    }

    @Test
    fun `wrong password is rejected`() {
        mockMvc.perform(
            get("/temperature-converter/celsius-fahrenheit/50")
                .with(httpBasic("user1", "wrong-password"))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `unknown user is rejected`() {
        mockMvc.perform(
            get("/temperature-converter/celsius-fahrenheit/50")
                .with(httpBasic("unknown", "pass1"))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `every API route requires authentication`() {
        mockMvc.perform(get("/temperature-converter/fahrenheit-celsius/32"))
            .andExpect(status().isUnauthorized)
    }
}
