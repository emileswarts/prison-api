package net.syscon.elite.service.impl;

import net.syscon.elite.api.model.CaseNote;
import net.syscon.elite.api.model.CaseNoteAmendment;
import net.syscon.elite.api.model.UserDetail;
import net.syscon.elite.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseNoteTransformerTest {

    @Mock
    private UserService userService;
    private CaseNoteTransformer transformer;
    private CaseNote caseNote;

    @Before
    public void init() {
        transformer = new CaseNoteTransformer(userService, "yyyy/MM/dd HH:mm:ss");
        caseNote = CaseNote.builder().amendments(new ArrayList<>()).build();

        when(userService.getUserByUsername(eq("MWILLIS_GEN"))).thenReturn(UserDetail.builder().staffId(-1L).firstName("MICHAEL").lastName("WILLIS").build());
        when(userService.getUserByUsername(eq("JOHN"))).thenReturn(UserDetail.builder().staffId(-2L).firstName("John").lastName("Saunders").build());
        when(userService.getUserByUsername(eq("SRENDELL"))).thenReturn(UserDetail.builder().staffId(-3L).firstName("Steven").lastName("MC'RENDELL").build());
    }

    @Test
    public void happyPathCaseNoteAmendmentTest() {
        caseNote.setText("test1 ...[MWILLIS_GEN updated the case notes on 2017/10/04 11:59:18] hi there ...[SRENDELL updated the case notes on 2017/10/04 12:00:06] hi again");
        final var returnedCaseNote = transformer.transform(caseNote);

        assertThat(returnedCaseNote.getOriginalNoteText()).isEqualTo("test1");
        assertThat(returnedCaseNote.getAmendments()).isEqualTo(List.of(
                new CaseNoteAmendment(LocalDateTime.parse("2017-10-04T11:59:18"), "Willis, Michael", "hi there"),
                new CaseNoteAmendment(LocalDateTime.parse("2017-10-04T12:00:06"), "Mc'rendell, Steven", "hi again")));
    }

    @Test
    public void happyPathCaseNoteAmendmentTest_OldNomisFormatDate() {
        caseNote.setText("test1 ...[MWILLIS_GEN updated the case notes on 04-10-2017 11:59:18] hi there ...[SRENDELL updated the case notes on 04-10-2017 12:00:06] hi again");
        final var returnedCaseNote = transformer.transform(caseNote);

        assertThat(returnedCaseNote.getOriginalNoteText()).isEqualTo("test1");
        assertThat(returnedCaseNote.getAmendments()).isEqualTo(List.of(
                new CaseNoteAmendment(LocalDateTime.parse("2017-10-04T11:59:18"), "Willis, Michael", "hi there"),
                new CaseNoteAmendment(LocalDateTime.parse("2017-10-04T12:00:06"), "Mc'rendell, Steven", "hi again")));
    }

    @Test
    public void badFormattedNoteTest() {
        caseNote.setText("a bad case note ...[MWILLIS_GEN updated thed case notes on 2017/10/04 11:59:18] hi there ..[SRENDELL updated the case notes on 2017/10/04 12:00:06] hi again ...[JOHN updated the case notes on 2016/12/31 23:59:06] only one!");
        final var returnedCaseNote = transformer.transform(caseNote);

        assertThat(returnedCaseNote.getOriginalNoteText()).isEqualTo("a bad case note ...[MWILLIS_GEN updated thed case notes on 2017/10/04 11:59:18] hi there ..[SRENDELL updated the case notes on 2017/10/04 12:00:06] hi again");
        assertThat(returnedCaseNote.getAmendments()).hasSize(1);

        final var firstAmendment = returnedCaseNote.getAmendments().get(0);
        assertThat(firstAmendment.getAdditionalNoteText()).isEqualTo("only one!");
        assertThat(firstAmendment.getAuthorName()).isEqualTo("Saunders, John");
        assertThat(firstAmendment.getCreationDateTime()).isEqualTo(LocalDateTime.of(2016, 12, 31, 23, 59, 06));
    }

    @Test
    public void singleCaseNoteTest() {
        caseNote.setText("Case Note with no ammendments");
        caseNote.setStaffId(-1L);

        final var returnedCaseNote = transformer.transform(caseNote);

        assertThat(returnedCaseNote.getOriginalNoteText()).isEqualTo("Case Note with no ammendments");
        assertThat(returnedCaseNote.getStaffId()).isEqualTo(-1L);
        assertThat(returnedCaseNote.getAmendments()).hasSize(0);
    }

    @Test
    public void badDateCaseNoteTest() {
        caseNote.setText("bad date ...[MWILLIS_GEN updated the case notes on 2017-10-04] bad one");
        final var returnedCaseNote = transformer.transform(caseNote);
        assertThat(returnedCaseNote.getOriginalNoteText()).isEqualTo("bad date");
        assertThat(returnedCaseNote.getAmendments()).hasSize(1);

        final var firstAmendment = returnedCaseNote.getAmendments().get(0);
        assertThat(firstAmendment.getAdditionalNoteText()).isEqualTo("bad one");
        assertThat(firstAmendment.getAuthorName()).isEqualTo("Willis, Michael");
        assertThat(firstAmendment.getCreationDateTime()).isNull();

    }

    @Test
    public void badUsernameCaseNoteTest() {
        caseNote.setText("bad user man ...[DUMMY_USER updated the case notes on 2017/01/31 12:23:43] bad user");
        final var returnedCaseNote = transformer.transform(caseNote);
        assertThat(returnedCaseNote.getOriginalNoteText()).isEqualTo("bad user man");
        assertThat(returnedCaseNote.getAmendments()).hasSize(1);

        final var firstAmendment = returnedCaseNote.getAmendments().get(0);
        assertThat(firstAmendment.getAdditionalNoteText()).isEqualTo("bad user");
        assertThat(firstAmendment.getAuthorName()).isEqualTo("DUMMY_USER");
        assertThat(firstAmendment.getCreationDateTime()).isEqualTo(LocalDateTime.of(2017, 1, 31, 12, 23, 43));
    }

    @Test
    public void emptyCaseNoteTest() {
        assertNull(transformer.transform(null));
        assertNull(transformer.transform(new CaseNote()));
    }
}
