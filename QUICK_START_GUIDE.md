# Quick Start Guide - Sprint 3, 4, 5 Implementation

## 🚀 Quick Setup (5 minutes)

### Step 1: Database Setup
Run the SQL migration to create new tables:

```bash
# Execute this SQL against your PostgreSQL database
psql -U postgres -d visadb -f - << 'EOF'

-- Sprint 3: Document Upload & OCR
CREATE TABLE IF NOT EXISTS document_file (
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

-- Sprint 4: QR Code Generation
CREATE TABLE IF NOT EXISTS qr_code_data (
    id BIGSERIAL PRIMARY KEY,
    id_demande BIGINT NOT NULL UNIQUE REFERENCES demande(id),
    qr_code_value TEXT NOT NULL,
    qr_code_image BYTEA,
    generated_date TIMESTAMP NOT NULL,
    request_token VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE
);

-- Sprint 5: Photo & Signature Capture
CREATE TABLE IF NOT EXISTS photo_capture (
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

CREATE TABLE IF NOT EXISTS signature_capture (
    id BIGSERIAL PRIMARY KEY,
    id_demandeur BIGINT NOT NULL REFERENCES demandeur(id),
    id_demande BIGINT REFERENCES demande(id),
    signature_data TEXT NOT NULL,
    signature_format VARCHAR(50),
    capture_date TIMESTAMP NOT NULL,
    file_name VARCHAR(255),
    is_valid BOOLEAN DEFAULT TRUE
);

EOF
```

### Step 2: Update Dependencies
Ensure `pom.xml` has been updated with new dependencies (already done):
```bash
mvn clean install
```

### Step 3: Create Upload Directory
```bash
mkdir -p uploads
chmod 755 uploads
```

### Step 4: Configure Application
Add to `application.yml`:
```yaml
app:
  upload-dir: uploads
```

### Step 5: Build & Run
```bash
mvn clean package
mvn spring-boot:run
```

---

## 📋 Feature Checklist

### Sprint 3: Document Upload & OCR ✅
- [x] File upload interface (PDF/Image)
- [x] Drag-and-drop support
- [x] OCR processing with Tesseract
- [x] Extracted text display
- [x] Confidence score calculation
- [x] Scan status tracking
- [x] File management (delete, reprocess)

### Sprint 4: Search & QR Code ✅
- [x] Search by request number
- [x] Search by passport number
- [x] QR code generation (ZXing)
- [x] Request token embedding
- [x] QR code download
- [x] FrontOffice REST API
- [x] Chronological sorting

### Sprint 5: Webcam & Signature ✅
- [x] Webcam photo capture
- [x] Signature drawing canvas
- [x] Photo validation
- [x] Demandeur file display
- [x] Primary photo management
- [x] Print functionality

---

## 🎯 Access Points

### Backoffice Application
| Feature | URL | Method |
|---------|-----|--------|
| Document Upload | `/backoffice/documents/upload/{demandeId}` | GET |
| Search | `/backoffice/search` | GET |
| QR Code | `/backoffice/qrcode/{demandeId}` | GET |
| Capture Photo/Signature | `/backoffice/photo-signature/capture/{demandeurId}` | GET |
| Demandeur File | `/backoffice/photo-signature/file/{demandeurId}` | GET |

### FrontOffice API
| Endpoint | URL | Purpose |
|----------|-----|---------|
| Scan QR | `/api/frontoffice/qr/{token}` | Scan QR code |
| Search Request | `/api/frontoffice/search/request/{id}` | Search by request |
| Search Passport | `/api/frontoffice/search/passport/{number}` | Search by passport |
| Health | `/api/frontoffice/health` | Check API status |
| Version | `/api/frontoffice/version` | Get API version |

---

## 💻 Example Usage

### Upload Document
```bash
curl -X POST \
  -F "file=@document.pdf" \
  http://localhost:8020/backoffice/documents/upload/1/1
```

### Scan QR Code
```bash
curl http://localhost:8020/api/frontoffice/qr/uuid-token-here
```

### Search Request
```bash
curl http://localhost:8020/api/frontoffice/search/request/123
```

### Search by Passport
```bash
curl http://localhost:8020/api/frontoffice/search/passport/AB123456
```

---

## 🎨 UI Overview

### Sprint 3: Document Upload Form
- Modern gradient header
- Piece justificative list
- Drag-and-drop zones
- Progress bars
- Extracted text display
- Summary statistics

### Sprint 4: Search Interface
- Tabbed search (request/passport)
- Real-time results
- Status badges
- Action buttons
- Responsive grid

### Sprint 4: QR Code Display
- Centered QR code
- Demandeur info
- Download button
- Request details

### Sprint 5: Photo/Signature Capture
- Webcam preview
- Photo capture button
- Signature canvas
- Status indicators
- Summary section

### Sprint 5: Demandeur File
- Photo display (200x250px)
- Complete information
- Signature display
- Print button
- Update button

---

## 🔧 Troubleshooting

### Issue: OCR not working
**Solution**: Install Tesseract on your system
```bash
# Linux
sudo apt-get install tesseract-ocr

# macOS
brew install tesseract

# Windows
Download from: https://github.com/UB-Mannheim/tesseract/wiki
```

### Issue: File upload fails
**Solution**: Check upload directory permissions
```bash
chmod 777 uploads
```

### Issue: Webcam not working
**Solution**: 
- Use HTTPS (required for getUserMedia)
- Check browser camera permissions
- Allow camera access in browser settings

### Issue: QR code not generating
**Solution**:
- Verify ZXing library is in classpath
- Check console for errors
- Ensure request token is unique

---

## 📊 Performance Optimization

### OCR Processing
- Optimize image size before OCR
- Use parallel processing for multiple documents
- Cache OCR results
- Implement timeout for OCR operations

### Database
- Create indexes on frequently searched columns:
```sql
CREATE INDEX idx_document_file_demande ON document_file(id_demande);
CREATE INDEX idx_qrcode_token ON qr_code_data(request_token);
CREATE INDEX idx_photo_demandeur ON photo_capture(id_demandeur);
CREATE INDEX idx_signature_demandeur ON signature_capture(id_demandeur);
```

### File Upload
- Implement chunked uploads for large files
- Compress images before storage
- Clean up old files periodically

---

## 🔐 Security Considerations

### File Upload
- ✅ File type validation
- ✅ File size limits (10MB)
- ✅ Filename sanitization
- ✅ Secure storage location

### API Security
- Add authentication for FrontOffice API
- Implement rate limiting
- Use HTTPS in production
- Validate all inputs
- Implement CORS properly

### Data Privacy
- Encrypt photo/signature data
- Implement access control
- Add audit logging
- Comply with GDPR

---

## 📈 Next Steps

1. **Testing**
   - Run unit tests for all services
   - Integration testing for workflows
   - User acceptance testing

2. **Deployment**
   - Set up CI/CD pipeline
   - Configure production database
   - Set up monitoring and logging

3. **Documentation**
   - Create user manuals
   - Update API documentation
   - Prepare training materials

4. **Enhancements**
   - Add more search filters
   - Implement document validation rules
   - Add email notifications
   - Create admin dashboard

---

## 📞 Support

For issues or questions:
1. Check the detailed documentation: `SPRINT_3_4_5_DOCUMENTATION.md`
2. Review error logs in console
3. Check browser developer tools
4. Contact development team

---

**Last Updated**: May 13, 2026
**Status**: Ready for Testing ✅
