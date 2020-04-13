package live.bokurano.evaluationclient.network

data class LoginResponse(val jwtToken: String, val userId: String, val userRole: String) {
}