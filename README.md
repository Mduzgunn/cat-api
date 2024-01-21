# Cat API

Bu uygulama, farklı kedi resimleri almak ve bu resimlere özel işlemler yapmak için kullanılan bir Spring Boot projesidir.

## Başlangıç

Uygulamayı yerel makinenizde çalıştırmak için aşağıdaki adımları takip edin.

### Gereksinimler

- Java 17 veya üzeri
- Maven

### Kurulum

1. Proje dizininde aşağıdaki komutu çalıştırarak Maven bağımlılıklarını indirin:

    ```bash
    mvn clean install
    ```

2. Uygulamayı başlatın:

    ```bash
    java -jar target/cat-0.0.1-SNAPSHOT.jar
    ```

## Kullanım

Uygulama başladığında, aşağıdaki endpoint'lere HTTP istekleri yapabilirsiniz.

### Özel Boyutlu Kedi Resmi Al

**Endpoint:**
GET /api/cat/custom?width={width}&height={height}&fileName={fileName}&directory={directory}


**Parametreler:**
- `width` (opsiyonel): Resim genişliği
- `height` (opsiyonel): Resim yüksekliği
- `fileName` (opsiyonel): İndirilen dosyanın adı
- `directory` (opsiyonel): İndirilen dosyanın kaydedileceği dizin

**Örnek Kullanım:**

GET /api/cat/custom?width=300&height=200&fileName=my_custom_cat.jpg&directory=/path/to/save



### Etiketlenmiş Kedi Resmi Al

**Endpoint:**
GET /api/cat/tagged?tag={tag}&fileName={fileName}&directory={directory}

**Parametreler:**
- `tag` (zorunlu): Kedi resminin etiketi
- `fileName` (opsiyonel): İndirilen dosyanın adı
- `directory` (opsiyonel): İndirilen dosyanın kaydedileceği dizin

**Örnek Kullanım:**
GET /api/cat/tagged?tag=cute&fileName=cute_cat.jpg&directory=/path/to/save


### Metin İçeren Kedi Resmi Al

**Endpoint:**
GET /api/cat/textual?text={text}&fileName={fileName}&directory={directory}


**Parametreler:**
- `text` (zorunlu): Kedi resminin üzerine eklenecek metin
- `fileName` (opsiyonel): İndirilen dosyanın adı
- `directory` (opsiyonel): İndirilen dosyanın kaydedileceği dizin

**Örnek Kullanım:**
GET /api/cat/textual?text=HelloCat&fileName=text_cat.jpg&directory=/path/to/save
