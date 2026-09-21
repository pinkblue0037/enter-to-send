# Enter to Send for ChatGPT (Android)

물리/블루투스 키보드를 사용할 때 ChatGPT Android 앱에서:

- **Enter** → 메시지 전송
- **Shift + Enter** → 줄바꿈
- ChatGPT 이외 앱에서는 Enter 키를 건드리지 않음

## 빌드

1. Android Studio에서 이 폴더를 엽니다.
2. Gradle Sync가 끝날 때까지 기다립니다.
3. `Build > Build APK(s)`를 선택합니다.
4. 생성된 `app-debug.apk`를 Android 기기에 설치합니다.

## 설치 후

1. `Enter to Send` 앱을 실행합니다.
2. `접근성 설정 열기`를 누릅니다.
3. 접근성 메뉴에서 **Enter to Send** 서비스를 활성화합니다.
4. ChatGPT 앱을 열고 블루투스 키보드로 테스트합니다.

## 참고

ChatGPT Android 앱의 접근성 트리에서 전송 버튼이 `Send`, `Send message`, `보내기`, `메시지 보내기`, `전송` 등의 라벨로 노출될 때 동작합니다.
ChatGPT 앱 UI가 바뀌어 전송 버튼의 접근성 라벨이 달라질 경우 `EnterToSendService.kt`의 `SEND_LABELS`에 새 라벨을 추가하면 됩니다.

앱은 접근성 권한을 사용하지만 네트워크 권한은 요청하지 않으며, ChatGPT가 활성 앱일 때 Enter 키와 전송 버튼만 처리합니다.

## Build APK with GitHub Actions (no Android Studio required)
1. Upload this project to a GitHub repository.
2. Open the repository's **Actions** tab.
3. Select **Build APK**.
4. Click **Run workflow**.
5. When the run finishes, open the run and download the **EnterToSendChatGPT-debug** artifact.
6. Unzip it and install `app-debug.apk` on Android.
