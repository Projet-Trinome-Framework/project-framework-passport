# Sprint Implementation Guide - Visa Management System

## Overview
This document describes the complete implementation of three sprints for the visa management system with modern, professional, and minimalist UI/UX.

---

## Sprint 3: Document Upload & OCR Processing

### Features
- ✅ File upload for checked justification pieces (PDF/Images)
- ✅ OCR processing to extract demandeur information
- ✅ "Scan terminated" status notification
- ✅ Extracted text display with confidence scores
- ✅ File management (upload, delete, process)

### Components

#### Entities
- **DocumentFile**: Stores uploaded documents with metadata
  - File path, size, type
  - Scan status (PENDING, IN_PROGRESS, COMPLETED, FAILED)
  - Extracted text and OCR confidence
  - Processing date and error messages

#### Services
- **FileUploadService**: Handles file operations
  - `uploadDocument()`: Upload and validate files
  - `processDocument()`: OCR processing with Tesseract
  - `getDocumentsByDemande()`: Retrieve documents
  - `deleteDocument()`: Remove uploaded files

#### Controllers
- **FileUploadController**: REST endpoints
  - `POST /backoffice/documents/upload/{demandeId}/{pieceId}`: Upload file
  - `GET /backoffice/documents/{demandeId}`: Get all documents
  - `POST /backoffice/documents/process/{documentId}`: Process document
  - `DELETE /backoffice/documents/{documentId}`: Delete document

#### UI Templates
- **document-upload-form.html**: Modern upload interface
  - Drag-and-drop file upload
  - Real-time progress tracking
  - OCR result display
  - Summary statistics
  - Responsive design with Tailwind CSS

### How to Use

1. Navigate to `/backoffice/documents/upload/{demandeId}`
2. Select a justification piece
3. Upload PDF or image file
4. File is automatically processed with OCR
5. Review extracted text and confidence score
6. Delete or reprocess as needed

### Technical Details
- **OCR Engine**: Tesseract (French + English support)
- **File Validation**: Max 10MB, PDF/JPEG/PNG only
- **Storage**: Disk-based (configurable via `app.upload-dir`)
- **Confidence Calculation**: Based on extracted text length

### Configuration
```yaml
app:
  upload-dir: uploads  # Directory for storing uploaded files
```

---

## Sprint 4: Search & QR Code Generation

### Features
- ✅ Search by request number OR passport number
- ✅ Chronological display for passport searches
- ✅ QR code generation when creating requests
- ✅ Unique request tokens embedded in QR codes
- ✅ New FrontOffice REST API for public access

### Components

#### Entities
- **QRCodeData**: Stores QR code information
  - QR code image (binary PNG)
  - Request token (UUID)
  - Generated date
  - Active status

#### Services
- **QRCodeService**: QR code management
  - `generateQRCode()`: Create QR code with ZXing
  - `getQRCodeByToken()`: Retrieve by token
  - `regenerateQRCode()`: Regenerate for existing request
  - `deactivateQRCode()`: Invalidate QR code

- **SearchService**: Search functionality
  - `searchByRequestNumber()`: Find by request ID
  - `searchByPassport()`: Find by passport (chronological)
  - `searchAdvanced()`: Search with filters

#### Controllers
- **QRCodeController**: QR code endpoints
  - `GET /backoffice/qrcode/{demandeId}`: Display QR code page
  - `GET /backoffice/qrcode/api/{demandeId}`: Get QR as JSON
  - `GET /backoffice/qrcode/token/{token}`: Scan QR code
  - `GET /backoffice/qrcode/download/{demandeId}`: Download QR image

- **SearchController**: Search endpoints
  - `GET /backoffice/search/request/{requestNumber}`: Search by request
  - `GET /backoffice/search/passport/{passeportNumber}`: Search by passport
  - `POST /backoffice/search/advanced`: Advanced search

- **FrontOfficeController**: Public REST API
  - `GET /api/frontoffice/qr/{token}`: Scan QR code
  - `GET /api/frontoffice/search/request/{requestNumber}`: Search request
  - `GET /api/frontoffice/search/passport/{passeportNumber}`: Search passport
  - `GET /api/frontoffice/health`: Health check
  - `GET /api/frontoffice/version`: Version info

#### UI Templates
- **search-form.html**: Professional search interface
  - Tabbed search (by request number or passport)
  - Real-time search results
  - Result details with status badge
  - Action buttons (view details, download)

- **qr-code-display.html**: QR code display page
  - Centered QR code with metadata
  - Demandeur information display
  - Download button

### How to Use

**Search by Request Number:**
1. Navigate to `/backoffice/search`
2. Enter request number
3. View matching requests

**Search by Passport:**
1. Click on "Par Passeport" tab
2. Enter passport number
3. View requests in chronological order

**Generate QR Code:**
1. Create a new request
2. QR code is automatically generated
3. Navigate to `/backoffice/qrcode/{demandeId}` to view
4. Download QR code image

**FrontOffice API:**
```bash
# Scan QR code
GET /api/frontoffice/qr/{token}

# Search by request
GET /api/frontoffice/search/request/123

# Search by passport
GET /api/frontoffice/search/passport/AB123456
```

### QR Code Data Structure
```json
{
  "token": "uuid-string",
  "demandeId": 123,
  "demandeurId": 456,
  "generatedAt": "2026-05-13T10:30:00"
}
```

---

## Sprint 5: Webcam & Signature Capture

### Features
- ✅ Webcam photo capture
- ✅ Signature capture (digital drawing)
- ✅ Photo + Signature completion status
- ✅ Demandeur file with photo and signature
- ✅ Primary photo management
- ✅ Multiple photos per demandeur

### Components

#### Entities
- **PhotoCapture**: Store captured photos
  - Photo data (binary image)
  - Photo format (JPEG/PNG)
  - Capture date
  - Primary flag
  - Quality score (for facial recognition)

- **SignatureCapture**: Store digitized signatures
  - Signature data (SVG or image)
  - Signature format
  - Capture date
  - Validity flag

#### Services
- **PhotoSignatureService**: Photo and signature management
  - `savePhoto()`: Save webcam photo
  - `saveSignature()`: Save signature
  - `getDemandeurFile()`: Complete demandeur file
  - `setPrimaryPhoto()`: Set as primary
  - `getPhotos()`: Get all photos for demandeur
  - `getSignatures()`: Get all signatures

#### Controllers
- **PhotoSignatureController**: Photo/Signature endpoints
  - `GET /backoffice/photo-signature/capture/{demandeurId}`: Capture page
  - `POST /backoffice/photo-signature/photo/{demandeurId}`: Save photo
  - `POST /backoffice/photo-signature/signature/{demandeurId}`: Save signature
  - `GET /backoffice/photo-signature/file/{demandeurId}`: View demandeur file
  - `GET /backoffice/photo-signature/photos/{demandeurId}`: Get photos
  - `PUT /backoffice/photo-signature/photo/{photoId}/primary`: Set primary

#### UI Templates
- **photo-signature-capture.html**: Capture interface
  - Live webcam preview
  - Photo capture button
  - Signature drawing canvas
  - Status indicators
  - Canvas-based signature drawing
  - Responsive grid layout

- **demandeur-file.html**: Demandeur profile
  - Photo display (200x250px frame)
  - Complete demandeur information
  - Signature display
  - Print functionality
  - Update button

### How to Use

**Capture Photo & Signature:**
1. Navigate to `/backoffice/photo-signature/capture/{demandeurId}`
2. Click "Démarrer" to start webcam
3. Click "Prendre" to capture photo
4. Draw signature on canvas
5. Click "Valider" to save signature
6. Click "Finaliser" to submit

**View Demandeur File:**
1. Navigate to `/backoffice/photo-signature/file/{demandeurId}`
2. View complete profile with photo and signature
3. Click "Imprimer" to print
4. Click "Mettre à Jour" to edit

### Technical Details
- **Webcam**: Browser MediaDevices API
- **Signature**: HTML5 Canvas with mouse tracking
- **Image Format**: Base64 encoded for transmission
- **Storage**: Binary data in database (BYTEA columns)

### Browser Requirements
- Modern browser with:
  - getUserMedia API support (webcam)
  - Canvas API support (signature)
  - FileReader API support

---

## Database Schema Changes

### New Tables

```sql
-- Sprint 3
CREATE TABLE document_file (
    id BIGSERIAL PRIMARY KEY,
    id_demande BIGINT NOT NULL REFERENCES demande(id),
    id_piece_justificative BIGINT NOT NULL REFERENCES piece_justificative(id),
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    file_type VARCHAR(100),
    upload_date TIMESTAMP NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    scan_status VARCHAR(50),
    extracted_text TEXT,
    ocr_confidence NUMERIC(5,2),
    processing_error TEXT,
    processing_date TIMESTAMP
);

-- Sprint 4
CREATE TABLE qr_code_data (
    id BIGSERIAL PRIMARY KEY,
    id_demande BIGINT NOT NULL UNIQUE REFERENCES demande(id),
    qr_code_value TEXT NOT NULL,
    qr_code_image BYTEA,
    generated_date TIMESTAMP NOT NULL,
    request_token VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE
);

-- Sprint 5
CREATE TABLE photo_capture (
    id BIGSERIAL PRIMARY KEY,
    id_demandeur BIGINT NOT NULL REFERENCES demandeur(id),
    id_demande BIGINT REFERENCES demande(id),
    photo_data BYTEA NOT NULL,
    photo_format VARCHAR(50),
    capture_date TIMESTAMP NOT NULL,
    file_name VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    quality_score NUMERIC(5,2)
);

CREATE TABLE signature_capture (
    id BIGSERIAL PRIMARY KEY,
    id_demandeur BIGINT NOT NULL REFERENCES demandeur(id),
    id_demande BIGINT REFERENCES demande(id),
    signature_data TEXT NOT NULL,
    signature_format VARCHAR(50),
    capture_date TIMESTAMP NOT NULL,
    file_name VARCHAR(255),
    is_valid BOOLEAN DEFAULT TRUE
);
```

---

## Dependencies Added

### pom.xml
```xml
<!-- OCR -->
<dependency>
    <groupId>net.sourceforge.tess4j</groupId>
    <artifactId>tess4j</artifactId>
    <version>5.10.0</version>
</dependency>

<!-- QR Code -->
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>core</artifactId>
    <version>3.5.3</version>
</dependency>
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.5.3</version>
</dependency>

<!-- Image Processing -->
<dependency>
    <groupId>org.imgscalr</groupId>
    <artifactId>imgscalr-lib</artifactId>
    <version>4.2</version>
</dependency>
```

---

## File Structure

```
src/main/java/fr/project/backoffice/
├── entity/
│   ├── DocumentFile.java
│   ├── QRCodeData.java
│   ├── PhotoCapture.java
│   └── SignatureCapture.java
├── repository/
│   ├── DocumentFileRepository.java
│   ├── QRCodeDataRepository.java
│   ├── PhotoCaptureRepository.java
│   └── SignatureCaptureRepository.java
├── service/
│   ├── FileUploadService.java
│   ├── QRCodeService.java
│   ├── SearchService.java
│   └── PhotoSignatureService.java
├── controller/
│   ├── FileUploadController.java
│   ├── QRCodeController.java
│   ├── SearchController.java
│   ├── PhotoSignatureController.java
│   └── FrontOfficeController.java
└── dto/
    ├── DocumentFileDto.java
    ├── QRCodeDto.java
    ├── SearchResultDto.java
    ├── PhotoCaptureDto.java
    ├── SignatureCaptureDto.java
    └── DemandeurFileDto.java

src/main/resources/templates/
├── document-upload-form.html
├── qr-code-display.html
├── search-form.html
├── photo-signature-capture.html
└── demandeur-file.html
```

---

## API Endpoints Summary

### Sprint 3 - Document Upload
```
POST   /backoffice/documents/upload/{demandeId}/{pieceId}     Upload file
GET    /backoffice/documents/{demandeId}                       Get documents
POST   /backoffice/documents/process/{documentId}              Process (OCR)
DELETE /backoffice/documents/{documentId}                      Delete document
```

### Sprint 4 - Search & QR Code
```
GET    /backoffice/search                                      Search form
GET    /backoffice/search/request/{requestNumber}              Search by request
GET    /backoffice/search/passport/{passeportNumber}           Search by passport
GET    /backoffice/qrcode/{demandeId}                          View QR code
GET    /backoffice/qrcode/api/{demandeId}                      QR code JSON
GET    /backoffice/qrcode/download/{demandeId}                 Download QR image
```

### Sprint 4 - FrontOffice API
```
GET    /api/frontoffice/qr/{token}                             Scan QR code
GET    /api/frontoffice/search/request/{requestNumber}         Search request
GET    /api/frontoffice/search/passport/{passeportNumber}      Search passport
GET    /api/frontoffice/health                                 Health check
GET    /api/frontoffice/version                                Version info
```

### Sprint 5 - Photo & Signature
```
GET    /backoffice/photo-signature/capture/{demandeurId}       Capture page
GET    /backoffice/photo-signature/file/{demandeurId}          View file
POST   /backoffice/photo-signature/photo/{demandeurId}         Save photo
POST   /backoffice/photo-signature/signature/{demandeurId}     Save signature
GET    /backoffice/photo-signature/photos/{demandeurId}        Get photos
GET    /backoffice/photo-signature/signatures/{demandeurId}    Get signatures
```

---

## UI/UX Features

### Design Principles
- ✅ **Minimalist**: Clean, spacious layouts
- ✅ **Modern**: Gradient backgrounds, rounded corners, smooth animations
- ✅ **Professional**: Consistent color scheme, clear typography
- ✅ **Responsive**: Mobile-first approach
- ✅ **Accessible**: Good contrast, readable fonts

### Color Palette
- **Primary**: #667eea (Purple-Blue)
- **Secondary**: #764ba2 (Dark Purple)
- **Success**: #2e7d32 (Green)
- **Error**: #d32f2f (Red)
- **Background**: Linear gradients with primary colors

### Components
- Custom styled buttons with hover effects
- Status badges (Pending, Processing, Completed, Failed)
- Progress bars for file uploads
- Drag-and-drop zones
- Tabbed interfaces
- Canvas for signature drawing
- Video preview for webcam
- Grid layouts for responsive design

---

## Testing the System

### Manual Testing Steps

**Sprint 3 - Document Upload:**
1. Create a new demande
2. Navigate to document upload form
3. Upload a PDF or image file
4. Verify OCR processing starts
5. Check extracted text is displayed
6. Verify confidence score calculation

**Sprint 4 - Search & QR Code:**
1. Generate QR code for a demande
2. Verify QR code is displayed
3. Search by request number
4. Search by passport number
5. Verify chronological ordering
6. Test FrontOffice API endpoints

**Sprint 5 - Photo & Signature:**
1. Navigate to capture page
2. Grant webcam permission
3. Capture photo
4. Draw signature
5. Submit both
6. Verify demandeur file displays
7. Test print functionality

---

## Configuration & Deployment

### Environment Variables
```properties
app.upload-dir=uploads
spring.datasource.url=jdbc:postgresql://localhost:5432/visadb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Running the Application
```bash
mvn clean install
mvn spring-boot:run
```

Access:
- Backoffice: http://localhost:8020
- FrontOffice API: http://localhost:8020/api/frontoffice

---

## Future Enhancements

### Sprint 6 Ideas
- [ ] PDF processing with PDFBox
- [ ] Facial recognition for photo quality
- [ ] Document validation rules
- [ ] Email notifications
- [ ] Dashboard with statistics
- [ ] Multi-language support
- [ ] Mobile app version

---

## Support & Documentation

For issues or questions:
1. Check error messages in logs
2. Verify database tables exist
3. Check file upload directory permissions
4. Verify browser supports WebRTC/Canvas APIs
5. Review console errors in browser

---

**Implementation Date**: May 13, 2026
**Status**: ✅ Complete
**Version**: 1.0.0
