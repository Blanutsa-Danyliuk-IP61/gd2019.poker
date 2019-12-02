import React from 'react';

import './styles.css';

const OptionsPanel = ({ options, callbacks }) => {

  function buttonStyles(disabled) {
      return{
          color: disabled ? 'grey' : 'black',
          backgroundColor: disabled ? 'lightgrey' : 'white'
      };
  }

  return (
    <div className='option-panel'>
        <h4>Options</h4>
        <div id='playerOptions'>
        <button
            className='option-button'
            onClick={() => callbacks.Deal('player')}
            style={buttonStyles(!options.Deal)}
            disabled={!options.Deal}
        >
            Deal
        </button>
        <button
            className='option-button'
            onClick={() => callbacks['New Game']('player')}
            style={buttonStyles(!options['New Game'])}
            disabled={!options['New Game']}
        >
            New Game
        </button>
        <button
            className='option-button'
            onClick={() => callbacks.Call('player')}
            style={buttonStyles(!options.Call)}
            disabled={!options.Call}
        >
            Call
        </button>
        <button
            className='option-button'
            onClick={() => callbacks.Fold('player')}
            style={buttonStyles(!options.Fold)}
            disabled={!options.Fold}
        >
            Fold
        </button>
      </div>
    </div>
  );
};

export default OptionsPanel;
