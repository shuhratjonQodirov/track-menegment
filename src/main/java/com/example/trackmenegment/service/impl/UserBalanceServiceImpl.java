package com.example.trackmenegment.service.impl;

import com.example.trackmenegment.dto.req.UserBalanceReqDto;
import com.example.trackmenegment.dto.res.UserBalanceResponse;
import com.example.trackmenegment.dto.res.UserBalanceTotalResDto;
import com.example.trackmenegment.enums.PaymentType;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.error.ValidationException;
import com.example.trackmenegment.mapper.UserBalanceMapper;
import com.example.trackmenegment.model.Trip;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.model.UserBalance;
import com.example.trackmenegment.repository.TripRepository;
import com.example.trackmenegment.repository.UserBalanceRepository;
import com.example.trackmenegment.repository.UserRepository;
import com.example.trackmenegment.service.UserBalanceService;
import com.example.trackmenegment.utils.ApiResponse;
import com.example.trackmenegment.validator.UserBalanceValidator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserBalanceServiceImpl implements UserBalanceService {
    private final UserBalanceRepository userBalanceRepository;
    private final UserRepository userRepository;
    private final UserBalanceValidator validator;
    private final UserBalanceMapper mapper;
    private final TripRepository tripRepository;

    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(37, 99, 235); // Blue-600
    private static final DeviceRgb SUCCESS_COLOR = new DeviceRgb(34, 197, 94); // Green-500
    private static final DeviceRgb DANGER_COLOR = new DeviceRgb(239, 68, 68); // Red-500
    private static final DeviceRgb WARNING_COLOR = new DeviceRgb(245, 158, 11); // Amber-500
    private static final DeviceRgb INFO_COLOR = new DeviceRgb(59, 130, 246); // Blue-500
    private static final DeviceRgb LIGHT_BG = new DeviceRgb(249, 250, 251); // Gray-50
    private static final DeviceRgb CARD_BG = new DeviceRgb(255, 255, 255); // White
    private static final DeviceRgb TEXT_GRAY = new DeviceRgb(107, 114, 128); // Gray-500
    private static final DeviceRgb BORDER_COLOR = new DeviceRgb(229, 231, 235); // Gray-200
    private static final DeviceRgb HEADER_BG = new DeviceRgb(30, 41, 59); // Slate-800

    @Override
    @Transactional
    public void createDriverBalance(User user, Trip trip, BigDecimal amountUsd, Boolean isPaid) {
        UserBalance balance = UserBalance.builder()
                .user(user)
                .trip(trip)
                .amountUsd(amountUsd)
                .paymentType(PaymentType.SALARY) // default ish haqi
                .description("Oylik ish haqi")
                .isPaid(isPaid)
                .operationDate(LocalDate.now())
                .build();
        userBalanceRepository.save(balance);
    }

    @Override
    @Transactional
    public ApiResponse create(UserBalanceReqDto dto) {

        validator.validateForCreate(dto);

        User user = userRepository.findByIdAndDeletedFalse(dto.getUserId()).orElseThrow(() -> new ByIdException("User not found"));
        Trip trip = null;
        if (dto.getTripId() != null) {
            trip = tripRepository.findByIdAndDeletedFalse(dto.getTripId()).orElseThrow(() -> new ByIdException("trip not found"));
        }
        UserBalance balance = mapper.toEntity(dto, user, trip);
        if (dto.getPaymentType() == PaymentType.PENALTY ||
                dto.getPaymentType() == PaymentType.PERSONAL ||
                dto.getPaymentType() == PaymentType.ADVANCE) {
            balance.setIsPaid(true);
        }
        userBalanceRepository.save(balance);
        return new ApiResponse(
                "balance successfully created", true
        );
    }

    @Override
    public ApiResponse getAll(Long userId) {
        User user = userRepository.findByIdAndDeletedFalse(userId).orElseThrow(() -> new ByIdException("User not found"));
        List<UserBalanceResponse> list = userBalanceRepository.findAllByUserAndDeletedFalseOrderByCreatedAtAsc(user).stream()
                .map(mapper::toDto)
                .toList();
        return new ApiResponse("List of user balance", true, list);
    }

    @Override
    public ApiResponse totalBalance(Long userId) {
        UserBalanceTotalResDto totalBalance = userBalanceRepository.totalBalance(userId).orElseThrow(() -> new ByIdException("Total balance not found"));
        return new ApiResponse("total date ", true, totalBalance);
    }

    @Override
    @Transactional
    public ApiResponse update(Long userBalanceId, UserBalanceReqDto dto) {
        UserBalance userBalance = userBalanceRepository.findByIdAndDeletedFalse(userBalanceId).orElseThrow(() -> new ByIdException("User Balance not found"));
        Trip trip = null;
        if (dto.getTripId() != null && dto.getTripId() != 0) {
            trip = tripRepository.findByIdAndDeletedFalse(dto.getTripId())
                    .orElseThrow(() -> new ByIdException("Ko'rsatilgan reys topilmadi"));
        }
        mapper.toUpdateEntity(userBalance, dto, trip);
        userBalanceRepository.save(userBalance);
        return new ApiResponse("Moliya yozuvi muvaffaqiyatli yangilandi", true, false);
    }

    @Override
    public byte[] getPdfUserBalance(Long userId) {

        try {
            User user = userRepository.findByIdAndDeletedFalse(userId)
                    .orElseThrow(() -> new ByIdException("User not found"));
            // Get balances
            List<UserBalance> balances = userBalanceRepository.findAllByUserAndDeletedFalse(user);

            // Generate PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            document.setMargins(30, 30, 30, 30);

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            if (balances.isEmpty()) {
                document.add(new Paragraph("Ma'lumot topilmadi!")
                        .setFont(boldFont)
                        .setFontSize(16)
                        .setTextAlignment(TextAlignment.CENTER));
                document.close();
                return baos.toByteArray();
            }

            // Calculate totals
            BigDecimal totalIncome = calculateIncome(balances);
            BigDecimal totalExpenses = calculateExpenses(balances);
            BigDecimal netBalance = totalIncome.subtract(totalExpenses);

            // Current year
            Integer currentYear = LocalDate.now().getYear();

            // Build PDF with modern design
            addModernHeader(document, boldFont, regularFont, user.getFullName(), currentYear);
            document.add(new Paragraph("\n"));

            addModernSummaryCards(document, boldFont, regularFont, totalIncome, totalExpenses, netBalance);
            document.add(new Paragraph("\n"));

            addModernBreakdown(document, boldFont, regularFont, balances);
            document.add(new Paragraph("\n").setMarginTop(5));

            addModernTransactionsTable(document, boldFont, regularFont, balances);

            addModernFooter(document, regularFont, userId);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new ValidationException("PDF yaratishda xatolik: " + e.getMessage(), e);
        }
    }

    private void addModernHeader(Document document, PdfFont boldFont, PdfFont regularFont,
                                 String userName, Integer year) throws Exception {
        // Header container with dark background
        Table headerTable = new Table(1).useAllAvailableWidth();

        Cell headerCell = new Cell()
                .setBackgroundColor(HEADER_BG)
                .setPadding(25)
                .setBorder(Border.NO_BORDER);

        // Icon + Title
        Paragraph title = new Paragraph("📊 MOLIYAVIY HISOBOT")
                .setFont(boldFont)
                .setFontSize(26)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(8);

        // Subtitle with year
        Paragraph subtitle = new Paragraph(year + "-yil uchun batafsil hisobot")
                .setFont(regularFont)
                .setFontSize(12)
                .setFontColor(new DeviceRgb(203, 213, 225)) // Slate-300
                .setTextAlignment(TextAlignment.CENTER);

        headerCell.add(title);
        headerCell.add(subtitle);
        headerTable.addCell(headerCell);

        document.add(headerTable);

        // User info card with modern style
        Table userInfoCard = new Table(1).useAllAvailableWidth();
        userInfoCard.setMarginTop(15);
        userInfoCard.setMarginBottom(15);

        Cell userInfoCell = new Cell()
                .setBackgroundColor(LIGHT_BG)
                .setBorder(new SolidBorder(BORDER_COLOR, 1))
                .setPadding(12);

        Paragraph userInfo = new Paragraph()
                .add(new Text("Xodim: ").setFont(regularFont).setFontSize(10).setFontColor(TEXT_GRAY))
                .add(new Text(userName).setFont(boldFont).setFontSize(11));

        Paragraph dateInfo = new Paragraph()
                .add(new Text("Hisobot sanasi: ").setFont(regularFont).setFontSize(10).setFontColor(TEXT_GRAY))
                .add(new Text(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .setFont(regularFont).setFontSize(10));

        userInfoCell.add(userInfo);
        userInfoCell.add(dateInfo);
        userInfoCard.addCell(userInfoCell);

        document.add(userInfoCard);
    }

    private void addModernSummaryCards(Document document, PdfFont boldFont, PdfFont regularFont,
                                       BigDecimal income, BigDecimal expenses, BigDecimal net) throws Exception {
        // Section title
        Paragraph sectionTitle = new Paragraph("ASOSIY KO'RSATKICHLAR")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginBottom(12);
        document.add(sectionTitle);

        // Three modern cards
        Table cardsTable = new Table(3).useAllAvailableWidth();
        cardsTable.setMarginBottom(15);

        // Card 1: Total Income (Green theme)
        Cell incomeCard = createModernCard(
                "💰",
                "Jami Daromad",
                "$" + formatMoney(income),
                "Maosh + Bonus",
                new DeviceRgb(240, 253, 244), // Green-50
                SUCCESS_COLOR,
                boldFont,
                regularFont
        );

        // Card 2: Total Expenses (Red theme)
        Cell expensesCard = createModernCard(
                "💸",
                "Jami Xarajat",
                "$" + formatMoney(expenses),
                "Avans + Shaxsiy + Jarima",
                new DeviceRgb(254, 242, 242), // Red-50
                DANGER_COLOR,
                boldFont,
                regularFont
        );

        // Card 3: Net Balance (Dynamic color)
        DeviceRgb balanceColor = net.compareTo(BigDecimal.ZERO) >= 0 ? SUCCESS_COLOR : DANGER_COLOR;
        DeviceRgb balanceBg = net.compareTo(BigDecimal.ZERO) >= 0 ?
                new DeviceRgb(240, 253, 244) : new DeviceRgb(254, 242, 242);
        String balanceIcon = net.compareTo(BigDecimal.ZERO) >= 0 ? "✅" : "⚠️";
        String balanceSubtitle = net.compareTo(BigDecimal.ZERO) >= 0 ? "Ijobiy balans" : "Salbiy balans";

        Cell balanceCard = createModernCard(
                balanceIcon,
                "Qolgan Balans",
                "$" + formatMoney(net),
                balanceSubtitle,
                balanceBg,
                balanceColor,
                boldFont,
                regularFont
        );

        cardsTable.addCell(incomeCard);
        cardsTable.addCell(expensesCard);
        cardsTable.addCell(balanceCard);

        document.add(cardsTable);
    }

    private Cell createModernCard(String icon, String title, String amount, String subtitle,
                                  DeviceRgb bgColor, DeviceRgb accentColor,
                                  PdfFont boldFont, PdfFont regularFont) {
        Cell card = new Cell()
                .setBackgroundColor(bgColor)
                .setPadding(15)
                .setBorder(new SolidBorder(accentColor, 2))
                .setMarginRight(5)
                .setMarginLeft(5);

        // Icon + Title
        Paragraph titlePara = new Paragraph()
                .add(new Text(icon + " ").setFontSize(16))
                .add(new Text(title).setFont(boldFont).setFontSize(9).setFontColor(TEXT_GRAY))
                .setMarginBottom(8);

        // Amount (large and bold)
        Paragraph amountPara = new Paragraph(amount)
                .setFont(boldFont)
                .setFontSize(22)
                .setFontColor(accentColor)
                .setMarginBottom(5);

        // Subtitle
        Paragraph subtitlePara = new Paragraph(subtitle)
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(TEXT_GRAY);

        card.add(titlePara);
        card.add(amountPara);
        card.add(subtitlePara);

        return card;
    }

    private void addModernBreakdown(Document document, PdfFont boldFont, PdfFont regularFont,
                                    List<UserBalance> balances) throws Exception {
        Paragraph breakdownTitle = new Paragraph("TURLAR BO'YICHA TAFSILOT")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginBottom(12);
        document.add(breakdownTitle);
        // Calculate by type
        BigDecimal salary = calculateByType(balances, PaymentType.SALARY);
        BigDecimal bonus = calculateByType(balances, PaymentType.BONUS);
        BigDecimal advance = calculateByType(balances, PaymentType.ADVANCE);
        BigDecimal personal = calculateByType(balances, PaymentType.PERSONAL);
        BigDecimal penalty = calculateByType(balances, PaymentType.PENALTY);

        Table breakdownTable = new Table(new float[]{1, 3, 2})
                .useAllAvailableWidth()
                .setBackgroundColor(ColorConstants.WHITE)
                .setBorder(new SolidBorder(BORDER_COLOR, 1));

        // Add rows with icons and progress bars
        addBreakdownRow(breakdownTable, "💵", "Maosh", salary, SUCCESS_COLOR, boldFont, regularFont);
        addBreakdownRow(breakdownTable, "🎁", "Bonus", bonus, SUCCESS_COLOR, boldFont, regularFont);
        addBreakdownRow(breakdownTable, "💰", "Avans", advance, WARNING_COLOR, boldFont, regularFont);
        addBreakdownRow(breakdownTable, "👤", "Shaxsiy", personal, INFO_COLOR, boldFont, regularFont);
        addBreakdownRow(breakdownTable, "⚠️", "Jarima", penalty, DANGER_COLOR, boldFont, regularFont);

        document.add(breakdownTable);
    }

    private void addBreakdownRow(Table table, String icon, String label, BigDecimal amount,
                                 DeviceRgb color, PdfFont boldFont, PdfFont regularFont) {
        // Icon cell
        Cell iconCell = new Cell()
                .add(new Paragraph(icon).setFontSize(18).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(LIGHT_BG)
                .setPadding(10);

        // Label cell
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(regularFont).setFontSize(11))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(LIGHT_BG)
                .setPadding(10);

        // Amount cell with colored background
        Cell amountCell = new Cell()
                .add(new Paragraph("$" + formatMoney(amount))
                        .setFont(boldFont)
                        .setFontSize(12)
                        .setFontColor(color)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(LIGHT_BG)
                .setPadding(10);

        table.addCell(iconCell);
        table.addCell(labelCell);
        table.addCell(amountCell);
    }

    private void addModernTransactionsTable(Document document, PdfFont boldFont, PdfFont regularFont,
                                            List<UserBalance> balances) throws Exception {
        Paragraph transactionsTitle = new Paragraph("BARCHA OPERATSIYALAR")
                .setFont(boldFont)
                .setFontSize(14)
                .setMarginBottom(12);
        document.add(transactionsTitle);

        Table table = new Table(new float[]{0.5f, 1.5f, 2.5f, 1.5f, 1.5f, 1})
                .useAllAvailableWidth()
                .setBackgroundColor(ColorConstants.WHITE);


        table.addHeaderCell(createModernHeaderCell("№", boldFont));
        table.addHeaderCell(createModernHeaderCell("Sana", boldFont));
        table.addHeaderCell(createModernHeaderCell("Tavsif", boldFont));
        table.addHeaderCell(createModernHeaderCell("Turi", boldFont));
        table.addHeaderCell(createModernHeaderCell("Summa", boldFont));
        table.addHeaderCell(createModernHeaderCell("Holat", boldFont));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        int index = 1;
        for (UserBalance balance : balances) {
            DeviceRgb rowBg = (index % 2 == 0) ? LIGHT_BG : (DeviceRgb) ColorConstants.WHITE;

            table.addCell(createModernDataCell(String.valueOf(index++), regularFont, rowBg));
            table.addCell(createModernDataCell(
                    balance.getOperationDate() != null ? balance.getOperationDate().format(formatter) : "-",
                    regularFont, rowBg));
            table.addCell(createModernDataCell(
                    balance.getDescription() != null ? balance.getDescription() : "-",
                    regularFont, rowBg));
            // Type cell with icon
            String typeIcon = getPaymentTypeIcon(balance.getPaymentType());
            String typeText = typeIcon + " " + getPaymentTypeInUzbek(balance.getPaymentType());
            table.addCell(createModernDataCell(typeText, regularFont, rowBg));

            DeviceRgb amountColor = isIncome(balance.getPaymentType()) ? SUCCESS_COLOR : DANGER_COLOR;
            String amountText = "$" + formatMoney(balance.getAmountUsd());
            Cell amountCell = createModernDataCell(amountText, boldFont, rowBg)
                    .setFontColor(amountColor)
                    .setTextAlignment(TextAlignment.RIGHT);
            table.addCell(amountCell);

            // Status cell with badge
            String statusText = balance.getIsPaid() != null && balance.getIsPaid() ? "✓ To'langan" : "○ Kutilmoqda";
            DeviceRgb statusColor = balance.getIsPaid() != null && balance.getIsPaid() ? SUCCESS_COLOR : TEXT_GRAY;
            table.addCell(createModernDataCell(statusText, regularFont, rowBg).setFontColor(statusColor));
        }

        document.add(table);
    }

    private Cell createModernHeaderCell(String text, PdfFont boldFont) {
        return new Cell()
                .add(new Paragraph(text).setFont(boldFont).setFontSize(9).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(PRIMARY_COLOR)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
    }

    private Cell createModernDataCell(String text, PdfFont font, DeviceRgb bgColor) {
        return new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(9))
                .setBackgroundColor(bgColor)
                .setBorder(Border.NO_BORDER)
                .setPadding(6);
    }

    private void addModernFooter(Document document, PdfFont regularFont, Long userId) throws Exception {
        document.add(new Paragraph("\n"));

        // Footer with separator
        LineSeparator separator = new LineSeparator(new SolidLine(1f));
        separator.setMarginTop(15);
        separator.setMarginBottom(15);
        document.add(separator);

        Table footerTable = new Table(new float[]{3, 1}).useAllAvailableWidth();

        Cell leftCell = new Cell();
        leftCell.setBorder(Border.NO_BORDER);

        Paragraph leftFooter = new Paragraph("Avtomatik yaratilgan hisobot")
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(TEXT_GRAY)
                .setMarginBottom(3);

        Paragraph dateFooter = new Paragraph("Sana: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(TEXT_GRAY)
                .setMarginBottom(3);

        Paragraph urlFooter = new Paragraph("PDF ni qayta yuklab olish uchun QR kodni skanerlang")
                .setFont(regularFont)
                .setFontSize(7)
                .setFontColor(TEXT_GRAY)
                .setItalic();

        leftCell.add(leftFooter);
        leftCell.add(dateFooter);
        leftCell.add(urlFooter);

        Cell rightCell = new Cell();
        rightCell.setBorder(Border.NO_BORDER);
        rightCell.setTextAlignment(TextAlignment.RIGHT);

        try {
            String reportUrl = "https://track-menegment-2.onrender.com/api/v1/user-balance/get-report-pdf/" + userId;
            byte[] qrCodeBytes = generateQRCode(reportUrl, 200, 200);

            Image qrImage = new Image(ImageDataFactory.create(qrCodeBytes));
            qrImage.setWidth(80);
            qrImage.setHeight(80);
            Table qrTable = new Table(1);
            qrTable.setBorder(new SolidBorder(BORDER_COLOR, 2));
            qrTable.setBackgroundColor(ColorConstants.WHITE);
            qrTable.setPadding(5);

            Cell qrCell = new Cell();
            qrCell.add(qrImage);
            qrCell.setBorder(Border.NO_BORDER);
            qrCell.setTextAlignment(TextAlignment.CENTER);

            Paragraph qrLabel = new Paragraph("Hisobotni yuklab olish")
                    .setFont(regularFont)
                    .setFontSize(7)
                    .setFontColor(TEXT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(3);

            qrCell.add(qrLabel);
            qrTable.addCell(qrCell);

            rightCell.add(qrTable);
        } catch (Exception e) {
            Paragraph linkText = new Paragraph("QR kod yaratilmadi")
                    .setFont(regularFont)
                    .setFontSize(8)
                    .setFontColor(DANGER_COLOR);
            rightCell.add(linkText);
        }

        footerTable.addCell(leftCell);
        footerTable.addCell(rightCell);

        document.add(footerTable);
    }

    private BigDecimal calculateIncome(List<UserBalance> balances) {
        return balances.stream()
                .filter(b -> b.getPaymentType() == PaymentType.SALARY ||
                        b.getPaymentType() == PaymentType.BONUS)
                .map(UserBalance::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateExpenses(List<UserBalance> balances) {
        return balances.stream()
                .filter(b -> b.getPaymentType() == PaymentType.ADVANCE ||
                        b.getPaymentType() == PaymentType.PERSONAL ||
                        b.getPaymentType() == PaymentType.PENALTY)
                .map(UserBalance::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateByType(List<UserBalance> balances, PaymentType type) {
        return balances.stream()
                .filter(b -> b.getPaymentType() == type)
                .map(UserBalance::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isIncome(PaymentType type) {
        return type == PaymentType.SALARY || type == PaymentType.BONUS;
    }

    private String getPaymentTypeInUzbek(PaymentType type) {
        return switch (type) {
            case SALARY -> "Maosh";
            case BONUS -> "Bonus";
            case ADVANCE -> "Avans";
            case PERSONAL -> "Shaxsiy";
            case PENALTY -> "Jarima";
            default -> type.name();
        };
    }

    private String getPaymentTypeIcon(PaymentType type) {
        return switch (type) {
            case SALARY -> "💵";
            case BONUS -> "🎁";
            case ADVANCE -> "💰";
            case PERSONAL -> "👤";
            case PENALTY -> "⚠️";
            default -> "📝";
        };
    }

    private String formatMoney(BigDecimal amount) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(amount);
    }

    private byte[] generateQRCode(String url, int width, int height) throws WriterException, Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "PNG", baos);
        return baos.toByteArray();
    }
}
