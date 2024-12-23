package br.com.tickets.orquestrator.tickets.services.feign.dto

data class PixInformationRequest(
    val addressKey: String,
    val description: String,
    val value: Int,
    val allowsMultiplePayments: Boolean = false,
    val expirationDate: String
)

data class PixInformationResponse(
    val id: String,
    val encodedImage: String,
    val payload: String,
    val allowsMultiplePayments: Boolean,
    val expirationDate: String
)
//Request
//"addressKey": "652d2b6a-9255-4f70-93ee-bc419ade8650",
//"description": "Ingresos evento luan buzanfa",
//"value": 250,
//"allowsMultiplePayments": false,
//"expirationDate": "2024-12-13 19:20:50"
//}

//Response
//{
//    "id": "TECHLIVI00000000560906ASA",
//    "encodedImage": "iVBORw0KGgoAAAANSUhEUgAAAcIAAAHCAQAAAABUY/ToAAADiElEQVR4Xu2XQW7bQBAEedv//yjP4k1xVc+StIFAhnPIBJiVzJ3t6WodZiXAx+uH69fxVfnuGvLdGvLd+ivyPFwrxToteNzKh/ZCZ9vuIbuSi8F+tKOkDllrdwwrx5B9yY+OXpuHEZR4NJthF1O5h2xOIkukY3FfgYC+hvwvSPvltLjACkvukO3JF00V6NRL3Yo33lLiHrItWROn+43Xvh9D/un1j8lr0WDGNXnnTiJHytJqDdmUdLZMWJAhn369635ELuYROmRXMg3RQlQqT9Jz2vj5G7IpmUMZLfAmTOexW4/bsX8ThmxInhjC037YCeJZwRoiDtmVvBriG0q1uBBXm7uRtfYv9ZANSdtbjIUjQ3fzcAeSWTdhyIak836I+5VjMTHt2JWbMGRHMiYE22lyoBWmMpKbcsimJMURi0i4mr+zN7IKnZRDNiUZ7sk1CJrySgLaiSI+z6+/8UO2IW3jux0pDSik7H5K1CG7kll6MK16loPNUzrRhuxLZraHf4Vd+8JUuthtG7IxieV2V1jO7PctqcO5f+OH7EciZOrcAXYpvPoiJ/EOHbIriSSs6Fs3z+LwFqN9yL6kUtrZaPDeiFfCkHLgGbIvmX78HGmcpaki7VhNZ/2nM2RHcsmGjEk7L0NzNvTyDdmbRC7meROQ6pjNvyFbk35n+dqiyIS4JfctJmTIrqRu+GUKYHFaE3R9gFlnfqmHbEjaDLYMqVYSgcIkcZU2ZFfShkz1N4MjWilxCg/ZmBSLsyAMn1Ko0977kE3JqJ7LDRA4EpbrFmzTkE1Jpsv3F8sLiihrGrHegWyqQ3YlUbTZpIGRtt7aTaiSx5BNyYcVF2fmXl7MdUlK0j5kW5KOVhyYPtWQYn7AQxqyK1nqqh66Xf8OYmzAG2p3yL6kVa2TUYdPFhdj7yblYgzZltxSQBlPp6T6HQCqNGRfUjbyDsFrisgOMtx6yK5kPIw6BodO70IOl4bcAwxDNiXT9mQppy9emWr5IRiH7EtKYYkcRCGuTYfb8pB9SdfFbnBtDWYb+BgNQzYlEQLBbfLFcb/iiCUPb8KQLcnlPYj5uhZ8w6+p+zZBTeOQbUkUnryltNS1iOT0941QGrI7uSizgZtRTFYC6zRkdzLTh8rDi0DzqEfCKnfItqRTlg8VJaphV1RYDUN2JRk57Ut22soyG0lOfcKQXckfriHfrSHfrX9C/gZ0wF56d4FB3wAAAABJRU5ErkJggg==",
//    "payload": "00020126900014br.gov.bcb.pix0136652d2b6a-9255-4f70-93ee-bc419ade86500228Ingresos evento luan buzanfa5204000053039865406250.005802BR5919Tech Livity SandBox6010Uruguaiana62290525TECHLIVI00000000560906ASA63041861",
//    "allowsMultiplePayments": false,
//    "expirationDate": "2024-12-13 19:20:50",
//    "externalReference": null
//}
