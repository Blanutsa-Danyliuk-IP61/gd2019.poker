import React, { Component } from 'react';
import { Typography, withStyles } from "@material-ui/core";

const styles = {
    time: {
        fontWeight: 'bold'
    }
};

class Timer extends Component {

    static defaultWaitTime = 60; //seconds

    constructor(props){
        super(props);

        this.state = {
            time: 0,
            isOn: false
        };

        this.startTimer = this.startTimer.bind(this);
    }

    startTimer() {
        this.setState({
            isOn: true,
            time: Timer.defaultWaitTime,
        });

        this.timer = setInterval(() => {
            if (this.state.time === 0 ) {
                clearInterval(this.timer);

                this.setState({
                    ...this.state,
                    time: 0
                });
            }

            this.setState({
                ...this.state,
                time: this.state.time - 1
            });
        }, 1000);
    }

    secondsToTime(seconds){
        const min = Math.floor(seconds / 60);
        const sec = seconds - min * 60;

        const getPrettyTime = time => {
            const str  = time + '';
            return str.length < 2 ? '0' + str : str;
        };

        return getPrettyTime(min) + ':' + getPrettyTime(sec);
    }

    render() {
        return (
            <Typography component='h6'  className={this.props.classes.time}>
                { this.secondsToTime(this.state.time) }
            </Typography>
        )
    }
}

export default withStyles(styles)(Timer);