import React from 'react';
import { connect } from 'react-redux';

import { Grid } from '@material-ui/core';
import PlayerPanel from '../PlayerPanel';
import Table from '../Table';
import OptionPanel from '../OptionsPanel';
import { getFirstPlayer, getPlayer, getSecondPlayer } from '../../util/redux/reducers/main';

import useStyles from './styles';
import BidPanel from "../BidPanel";

const GameArea = (props) => {

    const classes = useStyles();

    let { player, firstPlayer, secondPlayer } = props;

    return (
        <Grid container item xs={12} className={classes.root}>
            {/*<Grid key='player-1' item container xs={12} justify='center'>*/}
            {/*    <Grid item xs={3}>*/}
            {/*        <PlayerPanel*/}
            {/*            data={{
                            active: false,
                        }}
            {/*        />*/}
            {/*    </Grid>*/}
            {/*</Grid>*/}
            <Grid key='player-2-table-player-3' item container xs={12} justify='space-between' className={classes.tableContainer}>
                <Grid key='player-2' item xs={3}>
                    <PlayerPanel
                        shown={false}
                        playerData={firstPlayer}
                    />
                </Grid>
                <Grid key='table' item container xs={5} direction='column' justify='center'>
                    <Table cards={[]} />
                </Grid>
                <Grid key='player-3' item xs={3}>
                    <PlayerPanel
                        shown={false}
                        playerData={secondPlayer}
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
};

const mapStateToProps = state => ({
    firstPlayer: getFirstPlayer(state),
    secondPlayer: getSecondPlayer(state),
    player: getPlayer(state)
});

export default connect(mapStateToProps)(GameArea);