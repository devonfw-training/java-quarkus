#!/bin/bash

# Start Ollama in the background
/bin/ollama serve &
pid=$!

# Wait for Ollama to start
sleep 5

# Check if the ollama model exists
if ! ollama list | grep -q "^${OLLAMA_MODEL}"; then
    echo "${OLLAMA_MODEL} model not found. Pulling now..."
    ollama pull "${OLLAMA_MODEL}"
    echo "Model download complete!"
else
    echo "${OLLAMA_MODEL} model is already available."
fi

# Run the model with the specified keep-alive time
echo "Starting ${OLLAMA_MODEL} model with keep-alive time of ${OLLAMA_KEEP_ALIVE_TIME}..."
ollama run "${OLLAMA_MODEL}" --keepalive "${OLLAMA_KEEP_ALIVE_TIME}"

# Wait for Ollama process to finish
wait $pid