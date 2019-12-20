import React from 'react';

import { Dialog, DialogTitle } from '@material-ui/core';

function MessageDialog(props) {

    return (
        <Dialog open={props.open} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">{ props.text }</DialogTitle>
        </Dialog>
    );
}

export default MessageDialog;