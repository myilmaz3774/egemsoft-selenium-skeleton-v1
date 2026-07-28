# EGEMSOFT - Selenium Test Otomasyon Ödev Projesi

Bu proje, **EGEMSOFT** bünyesinde [https://automationexercise.com/](https://automationexercise.com/) web platformu üzerinde otomasyon çalışmaları gerçekleştirmek üzere tasarlanmış temel seviyedeki çalışma taslağıdır (skeleton).

---

## 📂 Proje Yapısı

```text
egemsoft-selenium-skeleton/
├── pom.xml                                     # Maven bağımlılıkları (Selenium & JUnit 5)
├── README.md                                   # Ödev ve görev açıklamaları
└── src/
    └── test/
        └── java/com/egemsoft/
            ├── AutomationTest.java            # Örnek başlangıç testi (Tarayıcı aç/kapat)
            ├── TestCase4LogoutTest.java       # [ÖDEV] Test Case 4: Logout User Taslağı
            └── ApiBonusTask.java              # [BONUS GÖREV] API 1: Ürün Listesi Taslağı
```

---

## 📌 1. Ana Ödev Görevi: UI Otomasyonu (Test Case 4: Logout User)

**Görev Dosyası:** `src/test/java/com/egemsoft/TestCase4LogoutTest.java`

Öğrencilerden aşağıdaki 10 adımı içeren Selenium test senaryosunu kodlamaları beklenmektedir:

1. Tarayıcıyı başlatın (Launch browser).
2. `http://automationexercise.com` adresine gidin.
3. Ana sayfanın başarıyla görüntülendiğini doğrulayın (Verify home page is visible).
4. **'Signup / Login'** butonuna tıklayın.
5. **'Login to your account'** yazısının görünür olduğunu doğrulayın.
6. Doğru e-posta adresi ve şifreyi girin.
7. **'login'** butonuna tıklayın.
8. **'Logged in as username'** yazısının görünür olduğunu doğrulayın.
9. **'Logout'** butonuna tıklayın.
10. Kullanıcının login sayfasına yönlendirildiğini doğrulayın.

---

## 🎁 2. Ekstra / Bonus Görev: API Otomasyonu (GET All Products List)

*(İnisiyatife bağlı ekstra görev)*

**Görev Dosyası:** `src/test/java/com/egemsoft/ApiBonusTask.java`

Vakti kalanlar için aşağıdaki API uç noktasına istek atarak dönen JSON yanıtındaki **ilk 10 ürünün adını** konsola yazdıran kurgunun tamamlanması beklenmektedir:

- **API URL:** `https://automationexercise.com/api/productsList`
- **Request Method:** `GET`
- **Response Code:** `200`
- **Beklenen Çıktı:** İlk 10 ürün adının konsola yazdırılması.

---

## 🚀 Projeyi Çalıştırma

### IDE (IntelliJ IDEA / Eclipse) Üzerinden
1. Projeyi IDE üzerinde açın.
2. `src/test/java/com/egemsoft` altındaki ilgili `.java` test sınıfını açın.
3. Metodun veya sınıfın yanındaki yeşil **Play (▶)** butonuna basarak çalıştırın.

### Maven Komut Satırından
```bash
mvn test
```

### Giriş testi için ortam değişkenleri

`TestCase4LogoutTest`, kullanıcı bilgilerini kaynak koda yazmaz. Testi çalıştırmadan önce geçerli bir Automation Exercise hesabının bilgileri ortam değişkeni olarak tanımlanmalıdır:

```powershell
$env:AUTOMATION_EXERCISE_EMAIL = "ornek@mail.com"
$env:AUTOMATION_EXERCISE_PASSWORD = "parola"
mvn -Dtest=TestCase4LogoutTest test
```

Bu değişkenler tanımlı değilse giriş testi başarısız olmak yerine atlanır. Bonus API testi bağımsız çalıştırılabilir:

```powershell
mvn -Dtest=ApiBonusTask test
```
