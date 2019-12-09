import React, {Fragment, useEffect, useState} from 'react';
import { useSelector } from 'react-redux';

import {Grid, Button, Typography, Slider, Avatar} from '@material-ui/core';

import useStyles from './styles';
import {getBalance} from "../../util/redux/reducers/main";
import coins from "../../assets/images/coins.png";

const minBit = 5;
const defaultWaitTime = 60; //seconds

const OptionPanel = (props) => {

    const classes = useStyles();
    const { callbacks } = props;
    const balance = useSelector(getBalance);

    const [ bit, setBit ] = useState(minBit);
    const [ seconds, setSeconds ] = useState(defaultWaitTime);

    useEffect(() => {
        debugger
        setTimeout(startTimer, 2000)
    }, []);

  const secondsToTime = seconds => {
      const min = Math.floor(seconds / 60);
      const sec = seconds - min * 60;

      const getPrettyTime = time => {
          const str  = time + '';
          return str.length < 2 ? '0' + str : str;
      };
      console.log(getPrettyTime(min) + ':' + getPrettyTime(sec));
      return getPrettyTime(min) + ':' + getPrettyTime(sec);
  };

  const startTimer = () => {
      const interval = setInterval(() => {
          if (seconds === 0) {
              clearInterval(interval);
              setSeconds(defaultWaitTime);
          }

          setSeconds(seconds - 1);
      }, 1000)
  };

  return (
      <Fragment>
          <Grid container item xs={12}>
              <Grid container item xs={4}>
                  <Button key='deal' variant='contained' className={classes.btn} onClick={() => callbacks.Deal('player')}>
                      Check
                  </Button>
              </Grid>
              <Grid container item xs={4}>
                  <Button key='call' variant='contained' className={classes.btn} onClick={() => callbacks.Deal('player')}>
                      Call
                  </Button>
              </Grid>
              { seconds > 0 ?
                  <Grid container item xs={4} justify='center'>
                      <Typography component='h6' className={classes.time}>
                          { secondsToTime(seconds) }
                      </Typography>
                   </Grid> : ''
              }
          </Grid>
          <Grid container item xs={12}>
              <Grid container item xs={4}>
                  <Button key='fold' variant='contained' className={classes.btn} onClick={() => callbacks.Deal('player')}>
                      Fold
                  </Button>
              </Grid>
              <Grid container item xs={4}>
                  <Button key='fold' variant='contained' className={classes.btn} onClick={() => callbacks.Deal('player')}>
                      Raise
                  </Button>
              </Grid>
          </Grid>
          <Grid container item xs={8} className={classes.btn}>
              <Typography className={classes.bit}>Bit</Typography>
              <Avatar src={coins} className={classes.coins}/>
              <Typography component='h6' className={classes.balance}>
                  { bit }
              </Typography>
              <Slider
                  className={classes.slider}
                  aria-label="custom thumb label"
                  min={minBit}
                  max={balance}
                  marks={[
                      {
                          value: minBit,
                          label: minBit,
                      },
                      {
                          value: balance,
                          label: balance,
                      },
                  ]}
                  onChange={(event, value) => setBit(value)}
                  color='secondary'
              />
          </Grid>
      </Fragment>
  );
};

export default OptionPanel;
