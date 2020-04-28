# mci-packages-android

Esse é um repositório de código aberto, desenvolvido pelo time da Merci, que disponibiliza algumas soluções que ajudarão desenvolvedores a acelerarem o processo de desenvolvimento.

## Módulo OTPAuth

Esse módulo diponibiliza a solução de geração de token (TOTP) a partir de uma url otp-auth. 
A seguir um exemplo de inicilização e acesso ao token gerado:

```kotlin
val otp = OTPAuth("otpauth://totp/XPTO:FOO?issuer=XPTO&algorithm=SHA1&digits=6&period=30&secret=N4SYQORWRZ2TIML5")
otp.currentToken()
```

Também é possível receber os eventos de timer da seguinte forma:
```kotlin
val otp = OTPAuth("otpauth://totp/XPTO:FOO?issuer=XPTO&algorithm=SHA1&digits=6&period=30&secret=N4SYQORWRZ2TIML5")
otp.handlerTimerRemaining {
    Log.i(
        "MainActivity",
        "Time Remaining -> ${it.remainingSeconds} - Token -> ${it.token}"
    )
}
```

Se quiser parar de ouvir os eventos:
```kotlin
otp.stopHandlerTimerRemaining()
```

[Merci @ 2020](https://merci.com.br)
