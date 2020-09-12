import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

Contract.make {
    description("should return all the Reservation")

    request {
        url("/reservations")
        method(HttpMethods.HttpMethod.GET.methodName)
    }

    response {
        status(HttpStatus.OK.value())
        body([[id: 1, name: "ps"]])
        headers {
            contentType(MediaType.APPLICATION_JSON_VALUE)
        }
    }
}