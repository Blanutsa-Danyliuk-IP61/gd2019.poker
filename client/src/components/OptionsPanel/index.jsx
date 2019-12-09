import React from 'react';
import { connect } from 'react-redux';

import { Button } from '@material-ui/core';

import './styles.css';

const OptionPanel = (props) => {

  const { callbacks } = props;

  return (
    <div className='option-panel'>
        <h4>Options</h4>
        <div>
            <Button
                key='deal'
                variant='contained'
                className='option-button'
                onClick={() => callbacks.Deal('player')}
            >
                Deal
            </Button>
            <Button
                key='call'
                variant='contained'
                className='option-button'
                onClick={() => callbacks.Deal('player')}
            >
                Call
            </Button>
            <Button
                key='fold'
                variant='contained'
                className='option-button'
                onClick={() => callbacks.Deal('player')}
            >
                Fold
            </Button>
      </div>
    </div>
  );
};

export default connect()(OptionPanel);
