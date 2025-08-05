package dev.httpmarco.polocloud.signs.abstraction

import dev.httpmarco.polocloud.signs.abstraction.data.sign.SignData

abstract class SignConnector(data : SignData) :  Connector<SignData.SignAnimationTick>(data)  {


}