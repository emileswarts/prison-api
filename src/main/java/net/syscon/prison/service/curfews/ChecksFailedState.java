package net.syscon.prison.service.curfews;

import net.syscon.prison.api.model.ApprovalStatus;
import net.syscon.prison.api.model.HdcChecks;
import net.syscon.prison.api.model.HomeDetentionCurfew;

class ChecksFailedState extends CurfewState {

    ChecksFailedState(HomeDetentionCurfew curfew) {
        super(curfew);
    }

    @Override
    void doSetHdcChecks(HdcChecks hdcChecks) {
        if (hdcChecks.getPassed()) {
            actions.deleteTrackings(StatusTrackingCodes.CHECKS_FAILED_CODES);
            actions.setHdcChecks(hdcChecks);
        } else if (!hdcChecks.getDate().equals(curfew.getChecksPassedDate())) {
            actions.setHdcChecksPassedDate(hdcChecks.getDate());
        }
    }

    @Override
    void doSetApprovalStatus(ApprovalStatus approvalStatus) {
        if (approvalStatus.isApproved()) {
            throw new IllegalArgumentException("The curfew checks are 'failed' (PASSED_FLAG = 'N'). You are not allowed to set the approval status to 'APPROVED' unless curfew checks are 'passed' (PASSED_FLAG = 'Y').");
        }

        actions.setApprovalStatus(approvalStatus);
        actions.setChecksFailedRefusedReason(approvalStatus.getRefusedReason());
    }

    @Override
    void doDeleteHdcChecks() {
        actions.deleteHdcChecks();
        actions.deleteTrackings(StatusTrackingCodes.CHECKS_FAILED_CODES);
    }

    @Override
    void doDeleteApprovalStatus() {
    }
}