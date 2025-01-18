package info.logbat.common.payload

import org.springframework.http.HttpStatus

class ApiCommonResponse<T> (
    private var statusCode : Int ,
    private var message : String,
    private var data : T
){
    companion object{
        private const val SUCCESS = "Success"
        @JvmStatic
        fun <T> createApiResponse(httpStatus:HttpStatus, message:String, data:T): ApiCommonResponse<T>{
            return ApiCommonResponse(httpStatus.value(), message, data)
        }
        @JvmStatic
        fun createFailResponse(httpStatus:HttpStatus, message:String): ApiCommonResponse<Unit>{
            return ApiCommonResponse(httpStatus.value(), message, Unit)
        }
        @JvmStatic
        fun createSuccessResponse(): ApiCommonResponse<Unit>{
            return ApiCommonResponse(HttpStatus.OK.value(), SUCCESS, Unit)
        }
        @JvmStatic
        fun <T> createSuccessResponse(data:T): ApiCommonResponse<T>{
            return ApiCommonResponse(HttpStatus.OK.value(), SUCCESS, data)
        }
    }
}