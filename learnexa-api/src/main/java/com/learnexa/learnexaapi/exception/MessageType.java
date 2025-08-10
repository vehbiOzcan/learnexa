package com.learnexa.learnexaapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MessageType {

    // Genel Hata Türleri
    UNAUTHORIZED("401", "Yetkisiz Erişim", "Bu işlem için kullanıcı kimliği doğrulaması gereklidir.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("403", "Yasaklı Erişim", "Sunucu isteği anlıyor fakat yetkilendirmeyi reddediyor.", HttpStatus.FORBIDDEN),
    NOT_FOUND("404", "Kaynak Bulunamadı", "Sunucu, istenilen kaynağı bulamıyor. Lütfen URL'yi kontrol edin.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("500", "İç Sunucu Hatası", "Sunucu, isteği yerine getirememesiyle ilgili beklenmedik bir durumla karşılaştı.", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("400", "Kötü İstek", "İstek, geçersiz veya eksik parametreler içeriyor. Lütfen tekrar deneyin.", HttpStatus.BAD_REQUEST),

    // JWT İlgili Hatalar
    JWT_EXPIRED("1001", "JWT Süresi Dolmuş", "JWT token süresi dolmuş. Lütfen token'ınızı yenileyin.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT("1002", "Geçersiz JWT", "Sağlanan JWT token geçersiz, lütfen yeniden giriş yapın.", HttpStatus.UNAUTHORIZED),
    MISSING_JWT("1003", "Eksik JWT", "JWT token eksik veya hatalı. Lütfen giriş yapın ve token'ı sağlayın.", HttpStatus.UNAUTHORIZED),
    JWT_NULL_OR_WRONG_FORMAT("1004", "Boş veya yanlış formatta JWT", "JWT token null veya yanlış formatta (gerekli format:Bearer ). Lütfen geçerli bir token sağlayın.", HttpStatus.UNAUTHORIZED),
    JWT_NOT_REFRESH("1005", "JWT Yenilenemedi", "JWT token yenilenemedi.", HttpStatus.UNAUTHORIZED),
    DEACTIVE_JWT("1006", "Kullanım Dışı JWT", "Sağlanan JWT token kullanım dışı", HttpStatus.UNAUTHORIZED),


    // Validation Hataları
    VALIDATION_ERROR("2001", "Doğrulama Hatası", "Bir doğrulama hatası oluştu. Lütfen giriş bilgilerinizi kontrol edin.", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED("2004", "Alan Zorunlu", "Bu alan boş bırakılamaz, lütfen tüm gerekli bilgileri doldurun.", HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT("2005", "Şifre Çok Kısa", "Şifre en az 6 karakter olmalıdır.", HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH("2006", "Şifre Uyuşmazlığı", "Girilen şifreler eşleşmiyor. Lütfen aynı şifreyi girin.", HttpStatus.BAD_REQUEST),
    PASSWORD_WRONG("2007", "Şifre Yanlış", "Girilen şifre kullanıcıya ait değil. Lütfen doğru şifreyi girin.", HttpStatus.BAD_REQUEST),

    // Kullanıcıya Ait Hatalar
    USER_NOT_FOUND("3001", "Kullanıcı Bulunamadı", "İstenilen kullanıcı bulunamadı. Lütfen kullanıcı adını kontrol edin.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("3002", "Kullanıcı Zaten Var", "Bu e-mail hesabı ile zaten bir kullanıcı mevcut. Lütfen farklı bir e-mail girin.", HttpStatus.CONFLICT),

    // Refresh Token İlgili Hatalar
    REFRESH_TOKEN_INVALID("4001", "Geçersiz Refresh Token", "Sağlanan refresh token geçersiz, lütfen yeniden giriş yapın.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("4002", "Süresi Dolmuş Refresh Token", "Refresh token süresi dolmuş. Lütfen yeni bir token almak için giriş yapın.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("4003", "Refresh Token Bulunamadı", "Sağlanan refresh token veritabanında bulunamadı.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NULL("4004", "Refresh Token Değeri Boş", "Refresh token null veya boş, lütfen geçerli bir token gönderin.", HttpStatus.BAD_REQUEST),

    // Rol İlgili Hatalar
    INSUFFICIENT_ROLE("5001", "Yetersiz Rol", "Bu kaynağa erişmek için yeterli yetkiniz yok. Lütfen yönetici ile iletişime geçin.", HttpStatus.FORBIDDEN),

    // File İşlemleri İle İlgili Hatalar
    FILE_NOT_FOUND("5001", "Dosya Bulunamadı", "İstenilen dosya bulunamadı. Lütfen dosya yolunu kontrol edin.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("5002", "Dosya Yükleme Başarısız", "Dosya yükleme işlemi sırasında bir hata oluştu. Lütfen tekrar deneyin.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SIZE_EXCEEDED("5003", "Dosya Boyutu Aşıldı", "Yüklenen dosya izin verilen maksimum boyutu aşıyor. Lütfen daha küçük bir dosya yükleyin.", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_SUPPORTED("5004", "Desteklenmeyen Dosya Türü", "Yüklenen dosya türü desteklenmiyor. Lütfen geçerli bir dosya türü seçin.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    FILE_NAME_TOO_LONG("5005", "Dosya Adı Çok Uzun", "Dosya adı izin verilen maksimum uzunluğu aşıyor. Lütfen daha kısa bir dosya adı kullanın.", HttpStatus.BAD_REQUEST),
    FILE_STORAGE_FAILED("5006", "Dosya Depolama Başarısız", "Dosya depolama işlemi sırasında bir hata oluştu. Lütfen tekrar deneyin.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_ALREADY_EXISTS("5007", "Dosya Zaten Var", "Aynı isimde bir dosya zaten mevcut. Lütfen farklı bir dosya adı seçin.", HttpStatus.CONFLICT),
    INVALID_FILE_PATH("5008", "Geçersiz Dosya Yolu", "Sağlanan dosya yolu geçersiz veya erişilemez. Lütfen geçerli bir yol sağlayın.", HttpStatus.BAD_REQUEST),
    FILE_EMPTY("5009", "Boş Dosya", "Yüklenen dosya boş. Lütfen geçerli bir dosya yükleyin.", HttpStatus.BAD_REQUEST),
    MULTIPART_RESOLUTION_FAILED("5010", "Dosya Çözümleme Hatası", "Multipart dosya çözümleme işlemi başarısız oldu. Lütfen dosyayı kontrol edin.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String title;
    private final String message;
    private final HttpStatus status;

    MessageType(String code, String title, String message, HttpStatus status) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.status = status;
    }

}
