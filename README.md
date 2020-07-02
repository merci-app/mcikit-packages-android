# mci-packages-android
Esse é um repositório de código aberto desenvolvido pelo time da Merci que disponibiliza algumas
soluções que irá ajudar desenvolvedores a acelerarem o seu processo de desenvolvimento

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

Se quiser parar de ouvir os eventos:
```kotlin
otp.stopHandlerTimerRemaining()
```

## Módulo de compoente de ui QRImageView
Esse módulo permite a exibição de um QRCode a partir de um códugo gerado. Para utilizar basta adicionar
o QRCodeImageView no layout, e para atualizar o QRCode de acordo com o código gerado bastas da seguite
forma:

Exemplo de utilização no Layout:

````xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.mcikit.qrimageview.QRImageView
        android:id="@+id/qrImageView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_gravity="center" />

</FrameLayout>
````
Exemplo de exibição do QRCode a a partir de um código. Nesse caso integramo o componente com o módulo
de mci-otpauth

```kotlin
OTPAuth("otpauth://totp/XPTO:FOO?issuer=XPTO&algorithm=SHA1&digits=6&period=30&secret=N4SYQORWRZ2TIML5")
    .handlerTimerRemaining {
        // Atualiza o QRImageView com o código gerado
        qrImageView.code = it.token
        Log.i(
            "MainActivity",
            "Time Remaining -> ${it.remainingSeconds} - Token -> ${it.token}"
        )
    }
```

[Merci @ 2020](https://merci.com.br)
