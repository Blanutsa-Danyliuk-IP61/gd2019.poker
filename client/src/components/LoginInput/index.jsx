import React, {useState} from 'react';
import { connect } from 'react-redux';
import { nameEntered } from '../../util/redux/reducers/main';

import { checkLogin } from '../../util/api';

import { Button, TextField, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Grid } from '@material-ui/core';

let entered = false;
let userName = '';

function LoginInputDialog(props) {

    const { dispatch } = props;
    const [ disableSave, setDisableSave ] = useState(true);
    const [ showError, setShowError ] = useState(false);

    const handleSave = () => {
       dispatch(nameEntered(userName));
    };

    const checkLoginIsUnique = (login) => {
        userName = login;
        entered = true;

        if (!login) {
            setDisableSave(true);
            return;
        }

        checkLogin(login, isUnique => {
            setDisableSave(!isUnique);
            if (entered) setShowError(!isUnique);
        } );
    };

    return (
        <Dialog open={props.open} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">Login</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    To start game, please enter your login here.
                </DialogContentText>
                <TextField
                    autoComplete="off"
                    error={ showError }
                    onChange={(event) => checkLoginIsUnique(event.currentTarget.value)}
                    autoFocus
                    margin="dense"
                    id="name"
                    label="Login"
                    type="text"
                    helperText={ showError ? "This login is already in use" : "" }
                    fullWidth
                />
            </DialogContent>
            <DialogActions>
                <Grid container justify='center'>
                    <Button onClick={handleSave}
                            color="primary"
                            disabled={disableSave}
                    >
                        Save
                    </Button>
                </Grid>
            </DialogActions>
        </Dialog>
    );
}

export default connect()(LoginInputDialog);