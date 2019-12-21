import React, { Component } from 'react';

import {Grid, withStyles} from '@material-ui/core';

import { connect } from 'react-redux';
import { getMessage, isMessageShown, isNewUser } from '../../util/redux/reducers/main';
import { connectWS } from '../../util/websocket';

import LoginInputDialog from '../../components/LoginInput';
import GameArea from '../GameArea';
import ChatAndInfoPanel from '../../components/ChatAndInfoPanel';
import MessageDialog from '../../components/MessageDialog';

const styles = {
    root: {
        margin: '0 auto',
        border: 0,
        color: 'white',
        padding: '30px',
        backgroundColor: 'rgb(0, 80, 0)',
        borderRadius: '20px',
    },
    charContainer: {
        height: '200px',
        border: '3px solid #411f18',
        borderRadius: '5px'
    }
};

class Poker extends Component {

    componentDidMount() {
        connectWS();
    }

    render() {

        const  { isNewUser, classes, isMessageShown, message } = this.props;

        return (
            <Grid container item xs={12} md={11} lg={10} className={classes.root}>
                <Grid
                    key='game-area'
                    item
                    xs={12}
                >
                    <GameArea />
                </Grid>
                <Grid
                    key='chat-info'
                    item
                    xs={12}
                >
                    <ChatAndInfoPanel />
                </Grid>

                <LoginInputDialog open={isNewUser}/>
                <MessageDialog open={isMessageShown} text={message}/>

            </Grid>
        );
    }
}

const mapStateToProps = state => ({
    isNewUser: isNewUser(state),
    isMessageShown: isMessageShown(state),
    message: getMessage(state)
});

export default connect(mapStateToProps)(withStyles(styles)(Poker));
