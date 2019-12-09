import React, {useState} from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { setLogin } from '../../util/redux/reducers/main';

import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import properties from '../../preperties';

let entered = false;
let userName = '';

function LoginInputDialog(props) {

    const { dispatch } = props;
    const [ disableSave, setDisableSave ] = useState(true);
    const [ showError, setShowError ] = useState(false);

    const handleSave = () => {
       dispatch(setLogin(userName));
    };

    const checkLogin = (login) => {
        userName = login;
        entered = true;

        if (!login) {
            setDisableSave(true);
            return;
        }

        axios.post(`${properties.apiUrl}/check/login`, {data: login})
            .then(res => {
                const isUnique = res.data;

                setDisableSave(!isUnique);
                if (entered) setShowError(!isUnique);
            })
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
                    onChange={(event) => checkLogin(event.currentTarget.value)}
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
                <Button onClick={handleSave}
                        color="primary"
                        disabled={disableSave}
                >
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default connect()(LoginInputDialog);