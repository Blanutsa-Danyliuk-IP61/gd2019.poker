import React, { Component } from 'react';

import {Grid, withStyles} from '@material-ui/core';
import { InfoMessagesQueue } from './../../util/infoMessagesQueue';

import { connect } from 'react-redux';
import { getLogin, isNewUser } from '../../util/redux/reducers/main';

import LoginInputDialog from '../LoginInput';
import GameArea from '../GameArea';
import ChatAndInfoPanel from '../ChatAndInfoPanel';

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

    constructor(props) {
        super(props);

        this.state = {
            playerData: {
                player: { id: 'player', active: true, hand: [] },
                ai1: { id: 'ai1', active: true, hand: [] },
                ai2: { id: 'ai2', active: true, hand: [] },
                ai3: { id: 'ai3', active: true, hand: [] },
              },
            tableCards: [],
            playerOptions: { Fold: false, Call: false, Deal: true, 'New Game': false },
            displayAICards: false,
            gameStage: 0,
            playerIsActive: true,
            infoMessages: new InfoMessagesQueue(),
        };
    }

    componentDidMount() {}

    render() {

        const  {isNewUser, classes } = this.props;

        return (
            <Grid container item xs={12} md={10} lg={9} className={classes.root}>
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
            </Grid>
        );
    }
}

const mapStateToProps = state => ({
    login: getLogin(state),
    isNewUser: isNewUser(state)
});

export default connect(mapStateToProps)(withStyles(styles)(Poker));
