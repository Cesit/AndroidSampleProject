package dk.cesit.androidsampleproject.backend;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Cesit on 10/02/2017.
 */

public class BackendResult {

    public final @Nullable String requestId;
    public final @Nullable BackendService.RequestType requestType;
    public final @Nullable String errorMessage;

    BackendResult(Intent intent) {
        this.requestId = intent.getStringExtra(BackendService.EXTRA_REQUEST_ID);
        String rawRequestType = intent.getStringExtra(BackendService.EXTRA_REQUEST_TYPE);
        this.requestType = BackendService.RequestType.valueOf(rawRequestType);
        this.errorMessage = intent.getStringExtra(BackendService.EXTRA_ERROR_MESSAGE);
    }
}
