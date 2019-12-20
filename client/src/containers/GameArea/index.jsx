import React, {Component} from 'react';
import { connect } from 'react-redux';

import {Grid, withStyles} from '@material-ui/core';
import PlayerPanel from '../PlayerPanel';
import Table from '../Table';
import OptionPanel from '../OptionsPanel';
import {
    getCurrentPlayerId,
    getFirstPlayer,
    getPlayer,
    getSecondPlayer,
    isGameActive
} from '../../util/redux/reducers/main';

import BidPanel from '../BidPanel';

const styles = {
    root: {
        height: '100%'
    },
    item: {
        height: '50%',
            border: '3px solid #411f18',
            borderRadius: '5px'
    },
    tableContainer: {
        margin: '10px 0'
    },
    bottomPlayers: {
        marginTop: '20px'
    }
};

class GameArea extends Component {

    render() {
        const {player, firstPlayer, secondPlayer, isGameActive, currentPlayerId, classes} = this.props;

        return (
            <Grid container item xs={12} className={classes.root}>
                <Grid key='player-2-table-player-3' item container xs={12} justify='space-between'
                      className={classes.tableContainer}>
                    <Grid key='player-2' item xs={3}>
                        <PlayerPanel
                            shown={false}
                            playerData={firstPlayer}
                            isGameActive={isGameActive}
                            currentPlayerId={currentPlayerId}
                        />
                    </Grid>
                    <Grid key='table' item container xs={5} direction='column' justify='center'>
                        <Table cards={[]}/>
                    </Grid>
                    <Grid key='player-3' item xs={3}>
                        <PlayerPanel
                            shown={false}
                            playerData={secondPlayer}
                            isGameActive={isGameActive}
                            currentPlayerId={currentPlayerId}
                        />
                    </Grid>
                </Grid>
                <Grid key='you' item container xs={12} className={classes.bottomPlayers}>
                    <Grid key='bid-panel' container item xs={4} justify='center'>
                        <Grid key='bid-panel' item xs={12}>
                            <BidPanel/>
                        </Grid>
                    </Grid>
                    <Grid key='player' item xs={4}>
                        <PlayerPanel
                            shown={true}
                            playerData={player}
                            isGameActive={isGameActive}
                            currentPlayerId={currentPlayerId}
                        />
                    </Grid>
                    <Grid key='player-dashboard' container item xs={4} justify='center'>
                        <Grid key='player-dashboard-ite,' item xs={9}>
                            <OptionPanel/>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
        );
    }
}

const mapStateToProps = (state) => ({
    firstPlayer: getFirstPlayer(state),
    secondPlayer: getSecondPlayer(state),
    player: getPlayer(state),
    isGameActive: isGameActive(state),
    currentPlayerId: getCurrentPlayerId(state)
});

export default connect(mapStateToProps)(withStyles(styles)(GameArea));