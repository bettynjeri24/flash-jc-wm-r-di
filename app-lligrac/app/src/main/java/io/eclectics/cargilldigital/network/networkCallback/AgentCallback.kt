package io.eclectics.cargill.network.networkCallback

object AgentCallback {
    data class LoginCallback(
    var name:String,
    var phone:String,
    var role:String,
    var email:String,
    var token:String,
    var agentNo:String,
    var cooperative:String,
    var cooperativeId:String
    )
}