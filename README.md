# mci-packages-android
Esse é um repositório de código aberto desenvolvido pelo time da Merci que disponibiliza de algumas
soluções que irá ajudar outras empresas a acelerarem o seu processo de desenvolvimento

## Módulo mci-otpauth
Esse módulo diponibiliza a solução de geração de token a partir de uma url de otp-auth. A seguir um exemplo
de inicilização e acesso ao token gerado:
```kotlin
val otp = OTPAuth("otpauth://totp/XPTO:FOO?issuer=XPTO&algorithm=SHA1&digits=6&period=30&secret=N4SYQORWRZ2TIML5")
otp.currentToken()
```

Também é possível receber os eventos de timer remaining seconds da seguinte forma:
```kotlin
val otp = OTPAuth("otpauth://totp/XPTO:FOO?issuer=XPTO&algorithm=SHA1&digits=6&period=30&secret=N4SYQORWRZ2TIML5")
otp.handlerTimerRemaining {
    Log.i(
        "MainActivity",
        "Time Remaining -> ${it.remainingSeconds} - Token -> ${it.token}"
    )
}
```

[Merci @ 2020](https://merci.com.br)
